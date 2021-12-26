import character.SpecEpDelta
import character.Stats
import character.classes.hunter.specs.BeastMastery
import character.classes.hunter.specs.Survival
import character.classes.mage.specs.Arcane
import character.classes.mage.specs.Fire
import character.classes.mage.specs.Frost
import character.classes.priest.specs.Shadow
import character.classes.rogue.specs.Assassination
import character.classes.rogue.specs.Combat
import character.classes.shaman.specs.Elemental
import character.classes.shaman.specs.Enhancement
import character.classes.warlock.specs.Affliction
import character.classes.warlock.specs.Destruction
import character.classes.warrior.specs.Arms
import character.classes.warrior.specs.Fury
import character.classes.warrior.specs.Kebab
import character.classes.warrior.specs.Protection
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
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
import mechanics.Rating
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

private val mapper = ObjectMapper().registerKotlinModule()

class TBCSim : CliktCommand() {
    val configFile: File? by argument(help = "Path to configuration file").file(mustExist = true).optional()
    val generate: Boolean by option("--generate", help="Autogenerate all item data").flag(default = false)
    val calcEP: Boolean by option("--calc-ep", help="Calculate EP values for every preset").flag(default = false)
    val calcRankings: Boolean by option("--calc-rankings", help="Calculate rankings for every preset").flag(default = false)
    val specFilterStr: String? by option("--specs", help="Limit rankings/ep calc by spec (comma-separated")
    val categoryFilterStr: String? by option("--categories", help="Limit rankings/ep calc by category (comma-separated")

    val duration: Int by option("-d", "--duration", help="Fight duration in seconds").int().default(SimDefaults.durationMs / 1000)
    val durationVariability: Int by option("-v", "--duration-variability", help="Varies the fight duration randomly, plus or minus zero to this number of seconds").int().default(SimDefaults.durationVaribilityMs / 1000)
    val stepMs: Int by option("-s", "--step-ms", help="Fight simulation step size, in milliseconds").int().default(SimDefaults.stepMs)
    val latencyMs: Int by option("-l", "--latency", help="Latency to add when casting spells, in milliseconds").int().default(SimDefaults.latencyMs)
    val iterations: Int by option("-i", "--iterations", help="Number of simulation iterations to run").int().default(SimDefaults.iterations)
    val targetLevel: Int by option("--target-level", help="Target level, from 70 to 73").int().default(SimDefaults.targetLevel).validate { it in 70..73 }
    val targetArmor: Int by option("-a", "--target-armor", help="The target's base armor value, before debuffs ").int().default(SimDefaults.targetArmor)
    val targetType: Int by option("-t", "--target-type", help="The target's type ordinal ").int().default(SimDefaults.targetType)
    val allowParryAndBlock: Boolean by option("-p", "--allow-parry-block").flag(default = SimDefaults.allowParryAndBlock)
    val showHiddenBuffs: Boolean by option("-b", "--show-hidden-buffs").flag(default = SimDefaults.showHiddenBuffs)
    val debug: Boolean by option("--debug").flag(default = false)

    val specs = mapOf(
        "hunter_bm" to BeastMastery(),
        "hunter_surv" to Survival(),
        "mage_arcane" to Arcane(),
        "mage_fire" to Fire(),
        "mage_frost" to Frost(),
        "priest_shadow" to Shadow(),
        "rogue_assassination" to Assassination(),
        "rogue_combat" to Combat(),
        "shaman_ele" to Elemental(),
        // Enhance weights aren't appreciably different between the two sub-specs
        "shaman_enh" to Enhancement(),
        "warlock_affliction_ruin" to Affliction(),
        "warlock_destruction_fire" to Destruction(),
        "warlock_destruction_shadow" to Destruction(),
        "warrior_arms" to Arms(),
        "warrior_fury" to Fury(),
        "warrior_kebab" to Kebab(),
        "warrior_protection" to Protection()
    )
    val presetPath = "./ui/src/presets/samples/"
    val rankingOutputPath = "./ui/src/rankings/data/ranks_all.json"
    val epOutputPath = "./ui/src/ep/data/ep_all.json"

    val presetsByCategory = listOf(
        "preraid" to mapOf(
            "hunter_bm" to File(presetPath + "hunter_bm_preraid.yml"),
            "hunter_surv" to File(presetPath + "hunter_surv_preraid.yml"),
            "mage_arcane" to File(presetPath + "mage_arcane_preraid.yml"),
            "mage_fire" to File(presetPath + "mage_fire_preraid.yml"),
            "mage_frost" to File(presetPath + "mage_frost_preraid.yml"),
            "priest_shadow" to File(presetPath + "priest_shadow_preraid.yml"),
            "rogue_assassination" to File(presetPath + "rogue_assassination_preraid.yml"),
            "rogue_combat" to File(presetPath + "rogue_combat_preraid.yml"),
            "shaman_ele" to File(presetPath + "shaman_ele_preraid.yml"),
            // Enhance weights aren't appreciably different between the two sub-specs
            "shaman_enh" to File(presetPath + "shaman_enh_subresto_preraid.yml"),
            "warlock_affliction_ruin" to File(presetPath + "warlock_affliction_ruin_preraid.yml"),
            "warlock_destruction_fire" to File(presetPath + "warlock_destruction_fire_preraid.yml"),
            "warlock_destruction_shadow" to File(presetPath + "warlock_destruction_shadow_preraid.yml"),
            "warrior_arms" to File(presetPath + "warrior_arms_preraid.yml"),
            "warrior_fury" to File(presetPath + "warrior_fury_preraid.yml"),
        ),
        "phase1" to mapOf(
            "hunter_bm" to File(presetPath + "hunter_bm_phase1.yml"),
            "hunter_surv" to File(presetPath + "hunter_surv_phase1.yml"),
            "mage_arcane" to File(presetPath + "mage_arcane_phase1.yml"),
            "mage_fire" to File(presetPath + "mage_fire_phase1.yml"),
            "mage_frost" to File(presetPath + "mage_frost_phase1.yml"),
            "priest_shadow" to File(presetPath + "priest_shadow_phase1.yml"),
            "rogue_assassination" to File(presetPath + "rogue_assassination_phase1.yml"),
            "rogue_combat" to File(presetPath + "rogue_combat_phase1.yml"),
            "shaman_ele" to File(presetPath + "shaman_ele_phase1.yml"),
            // Enhance weights aren't appreciably different between the two sub-specs
            "shaman_enh" to File(presetPath + "shaman_enh_subresto_phase1.yml"),
            "warlock_affliction_ruin" to File(presetPath + "warlock_affliction_ruin_phase1.yml"),
            "warlock_destruction_fire" to File(presetPath + "warlock_destruction_fire_phase1.yml"),
            "warlock_destruction_shadow" to File(presetPath + "warlock_destruction_shadow_phase1.yml"),
            "warrior_arms" to File(presetPath + "warrior_arms_phase1.yml"),
            "warrior_fury" to File(presetPath + "warrior_fury_phase1.yml"),
        ),
        "phase2" to mapOf(
            "hunter_bm" to File(presetPath + "hunter_bm_phase2.yml"),
            "hunter_surv" to File(presetPath + "hunter_surv_phase2.yml"),
            "mage_arcane" to File(presetPath + "mage_arcane_phase2.yml"),
            "mage_fire" to File(presetPath + "mage_fire_phase2.yml"),
            "mage_frost" to File(presetPath + "mage_frost_phase2.yml"),
            "priest_shadow" to File(presetPath + "priest_shadow_phase2.yml"),
            "rogue_assassination" to File(presetPath + "rogue_assassination_phase2.yml"),
            "rogue_combat" to File(presetPath + "rogue_combat_phase2.yml"),
            "shaman_ele" to File(presetPath + "shaman_ele_phase2.yml"),
            // Enhance weights aren't appreciably different between the two sub-specs
            "shaman_enh" to File(presetPath + "shaman_enh_subresto_phase2.yml"),
            "warlock_affliction_ruin" to File(presetPath + "warlock_affliction_ruin_phase2.yml"),
            "warlock_destruction_fire" to File(presetPath + "warlock_destruction_fire_phase2.yml"),
            "warlock_destruction_shadow" to File(presetPath + "warlock_destruction_shadow_phase2.yml"),
            "warrior_arms" to File(presetPath + "warrior_arms_phase2.yml"),
            "warrior_fury" to File(presetPath + "warrior_fury_phase2.yml"),
            "warrior_kebab" to File(presetPath + "warrior_kebab_phase2.yml"),
            "warrior_protection" to File(presetPath + "warrior_protection_phase2.yml"),
        )
    )

    fun singleEpSim(config: Config, opts: SimOptions, epDelta: SpecEpDelta? = null) : Pair<SpecEpDelta?, Double> {
        val epStatMod = epDelta?.second ?: Stats()
        val totalStatMod = Stats().add(epStatMod)//.add(hitReduction)

        val iterations = runBlocking { Sim(config, opts, totalStatMod) {}.sim() }
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

        // Use those deltas to compute socket deltas
        results["redSocket"] = formatEp(spec.redSocketEp(results))
        results["yellowSocket"] = formatEp(spec.yellowSocketEp(results))
        results["blueSocket"] = formatEp(spec.blueSocketEp(results))
        results["metaSocket"] = formatEp(spec.metaSocketEp(results))

        results.forEach {
            println("EP of one ${it.key}: ${it.value}")
        }

        return results
    }

    fun singleRankingSim(config: Config, opts: SimOptions) : Map<String, Double> {
        val iterations = runBlocking { Sim(config, opts) {}.sim() }

        val dps = SimStats.dps(iterations)
        val subjectMean = dps["subject"]!!.mean
        val petMean = dps["subjectPet"]?.mean ?: 0.0
        val totalMean = subjectMean + petMean

        val subjectMedian = dps["subject"]!!.median
        val petMedian = dps["subjectPet"]?.median ?: 0.0
        val totalMedian = subjectMedian + petMedian

        val subjectSd = dps["subject"]!!.sd
        val petSd = dps["subjectPet"]?.sd ?: 0.0

        return mapOf(
            "subjectMean" to subjectMean,
            "subjectPetMean" to petMean,
            "totalMean" to totalMean,

            "subjectMedian" to subjectMedian,
            "subjectPetMedian" to petMedian,
            "totalMedian" to totalMedian,

            "subjectSd" to subjectSd,
            "petSd" to petSd
        )
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
            targetType = targetType,
            allowParryAndBlock = allowParryAndBlock,
            showHiddenBuffs = showHiddenBuffs
        )

        val specFilter = specFilterStr?.split(",")
        val categoryFilter = categoryFilterStr?.split(",")

        if (calcEP) {
            val epTypeRef = object : TypeReference<EpOutput>(){}
            val existing = mapper.readValue(File(epOutputPath).readText(), epTypeRef)
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
            val epCategories =
                presetsByCategory.fold(mutableMapOf<String, Map<String, Map<String, Double>>>()) { acc, categoryEntry ->
                    if(categoryFilter == null || existing == null || existing.categories[categoryEntry.first] == null || categoryFilter.contains(categoryEntry.first)) {
                        acc[categoryEntry.first] =
                            categoryEntry.second.entries.fold(mutableMapOf()) { acc2, categorySpecEntry ->
                                if (specFilter == null || existing == null || existing.categories[categoryEntry.first] == null || specFilter.contains(
                                        categorySpecEntry.key
                                    )
                                ) {
                                    // Make config
                                    val config = ConfigMaker.fromYml(categorySpecEntry.value.readText())
                                    println("Starting EP run for ${categoryEntry.first}/${categorySpecEntry.key}")
                                    acc2[categorySpecEntry.key] = computeEpDeltas(config, opts)
                                } else {
                                    if(existing.categories[categoryEntry.first] != null && existing.categories[categoryEntry.first]!![categorySpecEntry.key] != null) {
                                        acc2[categorySpecEntry.key] =
                                            existing.categories[categoryEntry.first]!![categorySpecEntry.key]!!
                                    } else {
                                        println("No preexisting entry found for ${categoryEntry.first}/${categorySpecEntry.key} - skipping")
                                    }
                                }
                                acc2
                            }
                        acc
                    } else {
                        acc[categoryEntry.first] = existing.categories[categoryEntry.first]!!
                        acc
                    }
                }

            // Generate options/metadata
            val epOptions = specs.entries.fold(mutableMapOf<String, EpOutputOptions>()) { acc, entry ->
                acc[entry.key] = EpOutputOptions(
                    entry.value.benefitsFromMeleeWeaponDps,
                    entry.value.benefitsFromRangedWeaponDps
                )
                acc
            }

            // Output EPs
            val fullOutput = EpOutput(
                epCategories,
                epOptions
            )
            File(epOutputPath).writeText(Json { prettyPrint = true }.encodeToString(fullOutput))
        } else if (calcRankings) {
            val rankTypeRef = object : TypeReference<Map<String, Map<String, Map<String, Double>>>>(){}
            val existing = mapper.readValue(File(rankingOutputPath).readText(), rankTypeRef)
            val rankingCategories =
                presetsByCategory.fold(mutableMapOf<String, Map<String, Map<String, Double>>>()) { acc, categoryEntry ->
                    if(categoryFilter == null || existing == null || existing[categoryEntry.first] == null || categoryFilter.contains(categoryEntry.first)) {
                        acc[categoryEntry.first] =
                            categoryEntry.second.entries.fold(mutableMapOf()) { acc2, categorySpecEntry ->
                                // Make config
                                if (specFilter == null || existing == null || existing[categoryEntry.first] == null || specFilter.contains(
                                        categorySpecEntry.key
                                    )
                                ) {
                                    val config = ConfigMaker.fromYml(categorySpecEntry.value.readText())
                                    println("Starting ranking run for ${categoryEntry.first}/${categorySpecEntry.key}")
                                    acc2[categorySpecEntry.key] = singleRankingSim(config, opts)
                                } else {
                                    if(existing[categoryEntry.first] != null && existing[categoryEntry.first]!![categorySpecEntry.key] != null) {
                                        acc2[categorySpecEntry.key] =
                                            existing[categoryEntry.first]!![categorySpecEntry.key]!!
                                    } else {
                                        println("No preexisting entry found for ${categoryEntry.first}/${categorySpecEntry.key} - skipping")
                                    }
                                }
                                acc2
                            }
                        acc
                    } else {
                        acc[categoryEntry.first] = existing[categoryEntry.first]!!
                        acc
                    }
                }

            // Output rankings
            File(rankingOutputPath).writeText(Json { prettyPrint = true }.encodeToString(rankingCategories))
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
                val resourceTypes = config.character.klass.resourceTypes

                // Only print the big chart for the main subject - others arent interesting
                val resource = SimStats.resourceUsage(iterations)
                resourceTypes.forEach {
                    println("Resource usage for iteration ${resource[0][it.name]!!.iterationIdx}")
                    Chart.print(resource[0][it.name]!!.series, xMax = durationSeconds, yLabel = it.toString())
                }

                resourceTypes.forEach {
                    val resourceByAbility = SimStats.resourceUsageByAbility(iterations)
                    SimStatsPrinter.printResourceUsageByAbility(resourceByAbility)
                }

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

fun main(args: Array<String>) = TBCSim().main(args)
