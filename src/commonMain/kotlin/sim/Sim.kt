package sim

import character.Stats
import kotlinx.coroutines.*
import mu.KotlinLogging
import sim.config.Config
import sim.target.TargetProfiles
import util.Time
import kotlin.random.Random

class Sim (
    val config: Config,
    _opts: SimOptions,
    val epStatMod: Stats? = null,
    val progressCb:(SimProgress) -> Unit
) {
    val logger = KotlinLogging.logger {}

    // Merge in character file-defined options, if present
    // If we have a target profile, use that as well
    val targetProfileName = config.simOptions?.targetProfile
    val targetProfile = if(targetProfileName != null) TargetProfiles.profileFor(targetProfileName) else null

    val opts = _opts.merge(config.simOptions).merge(targetProfile?.simOptions)

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
        val iteration = SimIteration(config.character, config.rotation, opts, epStatMod, targetProfile)

        // Randomly alter the fight duration, if configured
        val dvms = opts.durationVariabilityMs
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
