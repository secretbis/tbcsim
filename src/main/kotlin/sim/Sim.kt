package sim

import kotlinx.coroutines.*
import mu.KotlinLogging
import sim.config.Config
import kotlin.random.Random

class Sim (
    val config: Config
) {
    val logger = KotlinLogging.logger {}

    suspend fun sim() {
        // Iteration coroutines
        val startTime = System.currentTimeMillis()
//        val iterationContext = newSingleThreadContext("IterationContext")

        val iterations = (1..config.opts.iterations).map {
            GlobalScope.async(Dispatchers.Default) {
//                withContext(iterationContext) {
                    iterate(it)
//                }
            }
        }.awaitAll()

        val endTime = System.currentTimeMillis()
        val totalTime = (endTime - startTime) / 1000
        println("Completed ${iterations.size} iterations in $totalTime seconds")

        // Stats
        SimStats.resultsByBuff(iterations)
        SimStats.resultsByDebuff(iterations)
        SimStats.resultsByDamageType(iterations)
        SimStats.resultsByAbility(iterations)
        SimStats.dps(iterations)
    }

    private fun iterate(num: Int) : SimIteration {
        // Simulate
        val iteration = SimIteration(config.character, config.rotation, config.opts)
        if(num == 1) {
            SimStats.precombatStats(iteration)
        }

        // Randomly alter the fight duration, if configured
        val dvms = config.opts.durationVariationMs
        val variability = if(dvms != 0) { Random.nextInt(-1 * dvms, dvms) } else 0
        val durationMs = config.opts.durationMs + variability

        for (timeMs in 0..durationMs step config.opts.stepMs) {
            iteration.tick++
            iteration.elapsedTimeMs = timeMs
            iteration.tick()
        }

        iteration.cleanup()

        return iteration
    }
}
