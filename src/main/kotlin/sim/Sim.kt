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
                iterate(it)
            }
        }.awaitAll()

        println("Completed ${iterations.size} iterations")

        // Stats
        Stats.resultsByBuff(iterations)
        Stats.resultsByDebuff(iterations)
        Stats.resultsByDamageType(iterations)
        Stats.resultsByAbility(iterations)
        Stats.dps(iterations)
    }

    private fun iterate(num: Int) : SimIteration {
        // Simulate
        val iteration = SimIteration(config.character, config.rotation, config.opts)
        if(num == 1) {
            Stats.precombatStats(iteration.subject, iteration.target)
        }

        for (timeMs in 0..config.opts.durationMs step config.opts.stepMs) {
            iteration.tick++
            iteration.elapsedTimeMs = timeMs
            iteration.tick()
        }

        iteration.cleanup()

        return iteration
    }
}
