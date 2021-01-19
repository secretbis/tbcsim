package sim

import mu.KotlinLogging
import java.text.DecimalFormat
import kotlin.math.sqrt

object Stats {
    val logger = KotlinLogging.logger {}

    val df = DecimalFormat("#,###.##")

    fun median(l: List<Double>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun sd(l: List<Double>, mean: Double) = sqrt(l.map { (it - mean) * (it - mean) }.average())

    fun dps(sim: Sim) {
        val perIteration = sim.iterations.map {
            it.events.fold(0.0) { acc, event ->
                acc + event.amount
            } / (sim.opts.durationMs / 1000.0)
        }

        val median = median(perIteration)
        val mean = perIteration.average()
        val sd = sd(perIteration, mean)

        logger.info("Median DPS: ${df.format(median)}")
        logger.info("Average DPS: ${df.format(mean)}")
        logger.info("Std. Dev: ${df.format(sd)}")
    }
}
