package sim

import character.Character
import kotlinx.coroutines.*
import mu.KotlinLogging
import sim.rotation.Rotation

class Sim (
    val subject: Character,
    val rotation: Rotation,
    val opts: SimOptions
) {
    val logger = KotlinLogging.logger {}

    suspend fun sim() {
        // Iteration coroutines
        val iterations = (1..opts.iterations).map {
            GlobalScope.async {
                iterate()
            }
        }.awaitAll()

        logger.info { "Completed ${iterations.size} iterations" }

        // Stats
        Stats.dps(iterations)
        Stats.resultsByAbility(iterations)
    }

    private fun iterate() : SimIteration {
        // Simulate
        val iteration = SimIteration(subject, rotation, opts)
        for (timeMs in 0..opts.durationMs step opts.stepMs) {
            iteration.tick++
            iteration.elapsedTimeMs = timeMs
            iteration.step()
        }

        return iteration
    }
}
