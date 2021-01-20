package sim

import de.m3y.kformat.Table
import de.m3y.kformat.table
import mu.KotlinLogging
import java.text.DecimalFormat
import kotlin.math.sqrt

object Stats {
    val logger = KotlinLogging.logger {}

    val df = DecimalFormat("#,###.##")

    fun median(l: List<Double>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun sd(l: List<Double>, mean: Double) = sqrt(l.map { (it - mean) * (it - mean) }.average())

    fun dps(iterations: List<SimIteration>) {
        val perIteration = iterations.map {
            it.events.filter { evt -> evt.eventType == Event.Type.DAMAGE }.fold(0.0) { acc, event ->
                acc + event.amount
            } / (it.opts.durationMs / 1000.0)
        }

        val median = median(perIteration)
        val mean = perIteration.average()
        val sd = sd(perIteration, mean)

        logger.info("Median DPS: ${df.format(median)}")
        logger.info("Average DPS: ${df.format(mean)}")
        logger.info("Std. Dev: ${df.format(sd)}")
    }

    fun resultsByAbility(iterations: List<SimIteration>) {
        val byAbility = iterations.flatMap { it.events }
            .filter { it.eventType == Event.Type.DAMAGE }
            .groupBy { it.ability.name }

        val keys = byAbility.keys.toList()

        logger.info {
            "\n" +
            table {
                // TODO: Rest of results
                header("Name", "Count", "TotalDmg", "AverageDmg", "MedianDmg", "StdDevDmg", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                for (key in keys) {
                    val events = byAbility[key]!!
                    val amounts = events.map { it.amount }
                    val count = amounts.size.toDouble()
                    val total = amounts.sum()

                    // Compute damage stats only for events where an attack actually connected
                    val nonzeroAmounts = amounts.filter { it > 0 }
                    val average = total / nonzeroAmounts.size.toDouble()
                    val median = median(nonzeroAmounts)
                    val sd = sd(nonzeroAmounts, average)

                    // Compute result distributions with the entire set of events
                    val hitPct = events.filter { it.result == Event.Result.HIT }.size / count * 100.0
                    val critPct = events.filter { it.result == Event.Result.CRIT }.size / count * 100.0
                    val missPct = events.filter { it.result == Event.Result.MISS }.size / count * 100.0
                    val dodgePct = events.filter { it.result == Event.Result.DODGE }.size / count * 100.0
                    val parryPct = events.filter { it.result == Event.Result.PARRY }.size / count * 100.0
                    val glancePct = events.filter { it.result == Event.Result.GLANCE }.size / count * 100.0

                    row(key, count, total, average, median, sd, hitPct, critPct, missPct, dodgePct, parryPct, glancePct)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    precision(1, 0)
                    precision(2, 2)
                    precision(3, 2)
                    precision(4, 2)
                    precision(5, 2)
                    precision(6, 2)
                    precision(7, 2)
                    precision(8, 2)
                    precision(9, 2)
                    precision(10, 2)
                    precision(11, 2)

                    postfix(6, "%")
                    postfix(7, "%")
                    postfix(8, "%")
                    postfix(9, "%")
                    postfix(10, "%")
                    postfix(11, "%")

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        }
    }
}
