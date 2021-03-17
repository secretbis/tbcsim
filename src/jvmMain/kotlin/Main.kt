import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import data.codegen.CodeGen
import kotlinx.coroutines.runBlocking
import sim.*
import sim.config.ConfigMaker
import java.io.File

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

    override fun run() {
        setupLogging(debug)

        if(generate) {
            CodeGen.generate()
        } else {
            if(configFile != null) {
                val config = ConfigMaker.fromYml(configFile!!.readText())
                val opts = SimOptions(
                    durationMs = duration * 1000,
                    durationVaribilityMs = durationVariability * 1000,
                    stepMs = stepMs,
                    latencyMs= latencyMs,
                    iterations = iterations,
                    targetLevel = targetLevel,
                    targetArmor = targetArmor,
                    allowParryAndBlock = allowParryAndBlock,
                    showHiddenBuffs = showHiddenBuffs
                )

                runBlocking {
                    val iterations = Sim(config, opts) {
                        if(it.iterationsCompleted == 1) {
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
            } else {
                println("Please specify a sim config file path as the first positional argument")
                println(this.getFormattedHelp())
            }
        }
    }
}

fun main(args: Array<String>) = TBCSim().main(args)
