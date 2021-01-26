import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import data.codegen.CodeGen
import kotlinx.coroutines.runBlocking
import sim.Sim
import sim.SimOptions
import sim.config.Config
import java.io.File

fun simOpts(): SimOptions {
    return SimOptions(iterations = 1000)
}

fun setupLogging() {
    val logKey = org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY
    if(System.getProperty(logKey).isNullOrEmpty()) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
    }
}

class TBCSim : CliktCommand() {
    val generate: Boolean by option("--generate", help="Autogenerate all item data").flag(default = false)
    val configFile: File by argument(help = "Path to configuration file").file(mustExist = true)

    override fun run() {
        setupLogging()

        if(generate) {
            CodeGen.generate()
        } else {
            val config = Config.fromYml(configFile)
            runBlocking {
                Sim(
                    config.character,
                    config.rotation,
                    config.opts
                ).sim()
            }
        }
    }
}

fun main(args: Array<String>) = TBCSim().main(args)
