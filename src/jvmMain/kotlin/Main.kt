import character.SpecEpDelta
import character.classes.hunter.specs.BeastMastery
import character.classes.hunter.specs.Survival
import character.classes.shaman.specs.Elemental
import character.classes.shaman.specs.Enhancement
import character.classes.warlock.specs.Affliction
import character.classes.warlock.specs.Destruction
import character.classes.warrior.specs.Arms
import character.classes.warrior.specs.Fury
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import data.codegen.CodeGen
import ep.EpOutput
import ep.EpOutputOptions
import kotlin.math.max
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import sim.*
import sim.config.Config
import sim.config.ConfigMaker
import java.io.File
import java.math.RoundingMode

fun setupLogging(debug: Boolean) {
    val level = if(debug) { "DEBUG" } else "INFO"
    val logKey = org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY
    if(System.getProperty(logKey).isNullOrEmpty()) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, level)
    }
}

class TBCSim : CliktCommand() {
    val configFile: File? by argument(help = "Path to configuration file").file(mustExist = true).optional()
    val generate: Boolean by option("--generate", help="Autogenerate all item data").flag(default = false)
    val calcEP: Boolean by option("--calc-ep", help="Calculate EP values for every preset").flag(default = false)
    val calcEPSingle: Boolean by option("--calc-ep-single", help="Calculate EP values a single character definition").flag(default = false)

    val duration: Int by option("-d", "--duration", help="Fight duration in seconds").int().default(SimDefaults.durationMs / 1000)
    val durationVariability: Int by option("-v", "--duration-variability", help="Varies the fight duration randomly, plus or minus zero to this number of seconds").int().default(SimDefaults.durationVaribilityMs / 1000)
    val stepMs: Int by option("-s", "--step-ms", help="Fight simulation step size, in milliseconds").int().default(SimDefaults.stepMs)
    val latencyMs: Int by option("-l", "--latency", help="Latency to add when casting spells, in milliseconds").int().default(SimDefaults.latencyMs)
    val iterations: Int by option("-i", "--iterations", help="Number of simulation iterations to run").int().default(SimDefaults.iterations)
    val targetLevel: Int by option("--target-level", help="Target level, from 70 to 73").int().default(SimDefaults.targetLevel).validate { it in 70..73 }
    val targetArmor: Int by option("-a", "--target-armor", help="The target's base armor value, before debuffs ").int().default(SimDefaults.targetArmor)
    val allowParryAndBlock: Boolean by option("-p", "--allow-parry-block").flag(default = SimDefaults.allowParryAndBlock)
    val showHiddenBuffs: Boolean by option("-b", "--show-hidden-buffs").flag(default = SimDefaults.showHiddenBuffs)
    val debug: Boolean by option("--debug").flag(default = false)

    val specs = mapOf(
        "hunter_bm" to BeastMastery(),
        "hunter_surv" to Survival(),
        "shaman_ele" to Elemental(),
        // Enhance weights aren't appreciably different between the two sub-specs
        "shaman_enh" to Enhancement(),
        "warlock_affliction_ruin" to Affliction(),
        "warlock_affliction_ua" to Affliction(),
        "warlock_destruction_fire" to Destruction(),
        "warlock_destruction_shadow" to Destruction(),
        "warrior_arms" to Arms(),
        "warrior_fury" to Fury(),
    )
    val presetPath = "./ui/src/presets/samples/"
    val epOutputPath = "./ui/src/ep/data/ep_all.json"
    val epPresetsByCategory = listOf(
        "preraid" to mapOf(
            "hunter_bm" to File(presetPath + "hunter_bm_preraid.yml"),
            "hunter_surv" to File(presetPath + "hunter_surv_preraid.yml"),
            "shaman_ele" to File(presetPath + "shaman_ele_preraid.yml"),
            // Enhance weights aren't appreciably different between the two sub-specs
            "shaman_enh" to File(presetPath + "shaman_enh_subresto_preraid.yml"),
            "warlock_affliction_ruin" to File(presetPath + "warlock_affliction_ruin_preraid.yml"),
            "warlock_affliction_ua" to File(presetPath + "warlock_affliction_ua_preraid.yml"),
            "warlock_destruction_fire" to File(presetPath + "warlock_destruction_fire_preraid.yml"),
            "warlock_destruction_shadow" to File(presetPath + "warlock_destruction_shadow_preraid.yml"),
            "warrior_arms" to File(presetPath + "warrior_arms_preraid.yml"),
            "warrior_fury" to File(presetPath + "warrior_fury_preraid.yml"),
        )
    )

    fun singleEpSim(config: Config, opts: SimOptions, epDelta: SpecEpDelta? = null) : Pair<SpecEpDelta?, Double> {
        val iterations = runBlocking { Sim(config, opts, epDelta?.second) {}.sim() }
        return Pair(epDelta, SimStats.dps(iterations).entries.sumByDouble { it.value?.mean ?: 0.0 })
    }

    fun formatEp(dps: Double): Double {
        return dps.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
    }

    fun computeEpDeltas(config: Config, opts: SimOptions): Map<String, Double> {
        // Run a baseline
        println("EP baseline")
        val baselineDpsMean = singleEpSim(config, opts)

        val results: MutableMap<String, Double> = mutableMapOf()

        // Run the base equivalence delta
        val spec = config.character.klass.spec
        println("EP base equivalence (${spec.epBaseStat.first})")
        val baseEquivalenceDpsMean = singleEpSim(config, opts, spec.epBaseStat)

        // Amount of DPS gained by one of our base equivalence unit
        val epBaseDpsMean = ((baseEquivalenceDpsMean.second - baselineDpsMean.second) / spec.epBaseStat.third)
        val epBaseDpsFactor = 1 / epBaseDpsMean

        // Store our base
        results[spec.epBaseStat.first] = formatEp(epBaseDpsMean * epBaseDpsFactor)

        // The rest of the stats
        spec.epStatDeltas.forEach { delta ->
            println("EP for ${delta.first}")
            val dps = singleEpSim(config, opts, delta)
            // If the sim comes out with a negative value, it's just worth zero and that run got unluckier than baseline
            val dpsDelta = max((dps.second - baselineDpsMean.second) / delta.third * epBaseDpsFactor, 0.0)
            results[delta.first] = formatEp(dpsDelta)
        }

        results.forEach {
            println("EP of one ${it.key}: ${it.value}")
        }

        return results
    }

    override fun run() {
        setupLogging(debug)

        if (generate) {
            CodeGen.generate()
            return
        }

        val opts = SimOptions(
            durationMs = duration * 1000,
            durationVaribilityMs = durationVariability * 1000,
            stepMs = stepMs,
            latencyMs = latencyMs,
            iterations = iterations,
            targetLevel = targetLevel,
            targetArmor = targetArmor,
            allowParryAndBlock = allowParryAndBlock,
            showHiddenBuffs = showHiddenBuffs
        )

        if (calcEP) {
            // EP calculation sim
            // Output looks like this:
            // {
            //   "categories": {
            //     "preraid": {
            //       "hunter_bm": {
            //         "rangedAttackPower": 1,
            //          ...
            //       },
            //       ...
            //     }
            //   }
            // }
            val epCategories = epPresetsByCategory.fold(mutableMapOf<String, Map<String, Map<String, Double>>>()) { acc, categoryEntry ->
                acc[categoryEntry.first] = categoryEntry.second.entries.fold(mutableMapOf()) { acc2, categorySpecEntry ->
                    // Make config
                    val config = ConfigMaker.fromYml(categorySpecEntry.value.readText())
                    println("Starting EP run for ${categorySpecEntry.key}")
                    acc2[categorySpecEntry.key] = computeEpDeltas(config, opts)
                    acc2
                }
                acc
            }

            // Generate options/metadata
            val epOptions = specs.entries.fold(mutableMapOf<String, EpOutputOptions>()) { acc, entry ->
                acc[entry.key] = EpOutputOptions(
                    entry.value.benefitsFromMeleeWeaponDps,
                    entry.value.benefitsFromRangedWeaponDps
                )
                acc
            }

            // Output
            val fullOutput = EpOutput(
                epCategories,
                epOptions
            )
            File(epOutputPath).writeText(Json.encodeToString(fullOutput))
        } else if(calcEPSingle) {
            if (configFile == null) {
                println("Please specify a sim config file path as the first positional argument")
                println(this.getFormattedHelp())
                return
            }

            val config = ConfigMaker.fromYml(configFile!!.readText())
            println("Starting EP run for ${configFile!!.name}")
            computeEpDeltas(config, opts)
        } else {
            if (configFile == null) {
                println("Please specify a sim config file path as the first positional argument")
                println(this.getFormattedHelp())
                return
            }

            val config = ConfigMaker.fromYml(configFile!!.readText())

            // Generic sim
            runBlocking {
                val iterations = Sim(config, opts) {
                    if (it.iterationsCompleted == 1) {
                        SimStatsPrinter.precombatStats(it.currentIteration)
                    }
                }.sim()

                // Stats
                val durationSeconds = (opts.durationMs / 1000.0).toInt()
                val resourceType = config.character.klass.resourceType
                val participants = iterations[0].participants
                participants.forEachIndexed { idx, sp ->
                    println("Participant #$idx")
                    val resource = SimStats.resourceUsage(iterations)
                    println("Resource usage for iteration ${resource[idx].iterationIdx}")
                    Chart.print(resource[idx].series, xMax = durationSeconds, yLabel = resourceType.toString())

                    val resourceByAbility = SimStats.resourceUsageByAbility(iterations)
                    SimStatsPrinter.printResourceUsageByAbility(resourceByAbility)

                    val buffs = SimStats.resultsByBuff(iterations)
                    SimStatsPrinter.printBuffs("Buffs", buffs)

                    val debuffs = SimStats.resultsByDebuff(iterations)
                    SimStatsPrinter.printBuffs("Debuffs", debuffs)

                    val dmgType = SimStats.resultsByDamageType(iterations)
                    SimStatsPrinter.printDamage(dmgType)

                    val abilities = SimStats.resultsByAbility(iterations)
                    SimStatsPrinter.printAbilities(abilities)

                    val dps = SimStats.dps(iterations)
                    SimStatsPrinter.printDps(dps["subject"]!!, dps["subjectPet"])
                }
            }
        }
    }
}

fun main(args: Array<String>) = TBCSim().main(args)
