package sim

import kotlinx.coroutines.*
import mu.KotlinLogging
import sim.config.Config

class Sim (
    val config: Config
) {
    val logger = KotlinLogging.logger {}

    suspend fun sim() {
        // Iteration coroutines
        val iterations = (1..config.opts.iterations).map {
            GlobalScope.async {
                iterate()
            }
        }.awaitAll()

        logger.info { "Completed ${iterations.size} iterations" }

        // Stats
        Stats.dps(iterations)
        Stats.resultsByAbility(iterations)
        Stats.resultsByBuff(iterations)
        Stats.resultsByDebuff(iterations)
    }

    private fun iterate() : SimIteration {
        // Simulate
        val iteration = SimIteration(config.character, config.rotation, config.opts)
        for (timeMs in 0..config.opts.durationMs step config.opts.stepMs) {
            iteration.tick++
            iteration.elapsedTimeMs = timeMs
            iteration.tick()
        }

        iteration.cleanup()

        return iteration
    }
}
