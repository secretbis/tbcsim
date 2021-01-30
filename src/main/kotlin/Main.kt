import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import data.codegen.CodeGen
import kotlinx.coroutines.runBlocking
import sim.Sim
import sim.SimOptions
import sim.config.Config
import java.io.File

fun setupLogging() {
    val logKey = org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY
    if(System.getProperty(logKey).isNullOrEmpty()) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
    }
}

class TBCSim : CliktCommand() {
    val configFile: File? by argument(help = "Path to configuration file").file(mustExist = true).optional()
    val generate: Boolean by option("--generate", help="Autogenerate all item data").flag(default = false)

    val duration: Int by option("-d", "--duration", help="Fight duration in seconds").int().default(180)
    val durationVariability: Int by option("-v", "--duration-variability", help="Varies the fight duration randomly, plus or minus zero to this number of seconds").int().default(0)
    val stepMs: Int by option("-s", "--step-ms", help="Fight simulation step size, in milliseconds").int().default(1)
    val latencyMs: Int by option("-l", "--latency", help="Latency to add when casting spells, in milliseconds").int().default(0)
    val iterations: Int by option("-i", "--iterations", help="Number of simulation iterations to run").int().default(100)
    val targetLevel: Int by option("--target-level", help="Target level, from 70 to 73").int().default(73).validate { it in 70..73 }
    val targetArmor: Int by option("-a", "--target-armor", help="The target's base armor value, before debuffs ").int().default(7700)
    val allowParryAndBlock: Boolean by option("-p", "--allow-parry-block").flag(default = false)

    override fun run() {
        setupLogging()

        if(generate) {
            CodeGen.generate()
        } else {
            if(configFile != null) {
                val config = Config.fromYml(configFile!!)
                val opts = SimOptions(
                    durationMs = duration * 1000,
                    durationVaribilityMs = durationVariability * 1000,
                    stepMs = stepMs,
                    latencyMs= latencyMs,
                    iterations = iterations,
                    targetLevel = targetLevel,
                    targetArmor = targetArmor,
                    allowParryAndBlock = allowParryAndBlock
                )

                runBlocking {
                    Sim(config, opts).sim()
                }
            } else {
                println("Please specify a sim config file path as the first positional argument")
                println(this.getFormattedHelp())
            }
        }
    }
}

fun main(args: Array<String>) = TBCSim().main(args)
