package sim

import character.Stats
import kotlinx.coroutines.*
import mu.KotlinLogging
import sim.config.Config
import util.Time
import kotlin.random.Random

class Sim (
    val config: Config,
    val opts: SimOptions,
    val epStatMod: Stats? = null,
    val progressCb:(SimProgress) -> Unit
) {
    val logger = KotlinLogging.logger {}

    suspend fun sim(): List<SimIteration> {
        // Iteration coroutines
        val startTime = Time.currentTimeMillis()

        val iterations = (1..opts.iterations).map {
            GlobalScope.async {
                val iteration = iterate(it)

                progressCb(SimProgress(
                    opts,
                    it,
                    iteration
                ))

                iteration
            }
        }.awaitAll()

        val endTime = Time.currentTimeMillis()
        val totalTime = endTime.minus(startTime) / 1000.0
        println("Completed ${iterations.size} iterations in $totalTime seconds")

        return iterations
    }

    private fun iterate(num: Int) : SimIteration {
        // Simulate
        val iteration = SimIteration(config.character, config.rotation, opts, epStatMod)

        // Randomly alter the fight duration, if configured
        val dvms = opts.durationVaribilityMs
        val variability = if(dvms != 0) { Random.nextInt(-1 * dvms, dvms) } else 0
        val durationMs = opts.durationMs + variability

        for (timeMs in 0..durationMs step opts.stepMs) {
            iteration.tickNum++
            iteration.elapsedTimeMs = timeMs
            iteration.tick()
        }

        iteration.cleanup()

        return iteration
    }
}
