package sim

import character.Buff
import de.m3y.kformat.Table
import de.m3y.kformat.table
import mu.KotlinLogging
import java.text.DecimalFormat
import kotlin.math.sqrt

object Stats {
    val logger = KotlinLogging.logger {}

    data class BuffSegment(
        val startMs: Int,
        val endMs: Int,
        val refreshCount: Int,
        val buff: Buff
    ) {
        val durationMs: Int
            get() = endMs - startMs
    }

    data class AbilityBreakdown(
        val name: String,
        val count: Int,
        val total: Double,
        val average: Double,
        val median: Double,
        val sd: Double,
        val hitPct: Double,
        val critPct: Double,
        val missPct: Double,
        val dodgePct: Double,
        val parryPct: Double,
        val glancePct: Double,
    )

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

    fun resultsByBuff(iterations: List<SimIteration>) {
        val byBuff = iterations.flatMap { it.events }
            .filter {
                it.buff != null && !it.buff.hidden && (
                    it.eventType == Event.Type.BUFF_START ||
                    it.eventType == Event.Type.BUFF_REFRESH ||
                    it.eventType == Event.Type.BUFF_END
                )
            }
            .groupBy { it.buff!!.name }

        val keys = byBuff.keys.toList()

        logger.info {
            "Buffs\n" +
            table {
                header("Name", "AppliedCount", "RefreshedCount", "UptimePct", "AvgDurationSeconds")

                for (key in keys) {
                    val events = byBuff[key]!!
                    val applied = events.filter { it.eventType == Event.Type.BUFF_START }.size
                    val refreshed = events.filter { it.eventType == Event.Type.BUFF_REFRESH }.size

                    val segments = mutableListOf<BuffSegment>()
                    var lastEvent: Event? = null
                    var currentStart: Event? = null
                    var refreshCount: Int = 0
                    events.forEach {
                        if(it.eventType == Event.Type.BUFF_START) {
                            if(lastEvent?.eventType == Event.Type.BUFF_START) {
                                logger.warn { "Possibly invalid segment - found two starts for buff: ${it.buff!!.name}" }
                            }

                            currentStart = it
                        }

                        if(it.eventType == Event.Type.BUFF_REFRESH) {
                            if(lastEvent?.eventType == Event.Type.BUFF_END) {
                                logger.warn { "Possibly invalid segment - refresh without enclosing start for buff: ${it.buff!!.name}" }
                            }
                            refreshCount++
                        }

                        if(it.eventType == Event.Type.BUFF_END) {
                            if(lastEvent?.eventType == Event.Type.BUFF_END) {
                                logger.warn { "Possibly invalid segment - found two ends for buff: ${it.buff!!.name}" }
                            }

                            if(currentStart != null) {
                                segments.add(BuffSegment(currentStart!!.timeMs, it.timeMs, refreshCount, it.buff!!))
                                // Reset
                                currentStart = null
                                refreshCount = 0
                            } else {
                                logger.warn { "Possibly invalid segment - found end without start for buff: ${it.buff!!.name}" }
                            }
                        }

                        lastEvent = it
                    }

                    val uptimePct = segments.map { it.durationMs }.sum() / (iterations.size * iterations[0].opts.durationMs) * 100.0
                    val avgDuration = segments.map { it.durationMs }.sum() / segments.size.toDouble() / 1000.0

                    row(key, applied, refreshed, uptimePct, avgDuration)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    for(i in 1..2) {
                        precision(1, 0)
                    }

                    for(i in 3..4) {
                        precision(i, 2)
                    }

                    for(i in 1..4) {
                        formatFlag(i, ",")
                    }

                    for(i in 3..3) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        }
    }

    fun resultsByAbility(iterations: List<SimIteration>) {
        val byAbility = iterations.flatMap { it.events }
            .filter { it.eventType == Event.Type.DAMAGE }
            .groupBy { it.ability?.name ?: "Unknown" }

        val keys = byAbility.keys.toList()

        logger.info {
            "Ability Breakdown\n" +
            table {
                header("Name", "Count", "TotalDmg", "AverageDmg", "MedianDmg", "StdDevDmg", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                val rows = keys.map { key ->
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
                    // Count blocked crits as crits, since the block value is very small
                    val hitPct = events.filter { it.result == Event.Result.HIT }.size / count * 100.0
                    val critPct = events.filter { it.result == Event.Result.CRIT || it.result == Event.Result.BLOCKED_CRIT }.size / count * 100.0
                    val missPct = events.filter { it.result == Event.Result.MISS }.size / count * 100.0
                    val dodgePct = events.filter { it.result == Event.Result.DODGE }.size / count * 100.0
                    val parryPct = events.filter { it.result == Event.Result.PARRY }.size / count * 100.0
                    val glancePct = events.filter { it.result == Event.Result.GLANCE }.size / count * 100.0

                    AbilityBreakdown(
                        key,
                        count.toInt(),
                        total,
                        average,
                        median,
                        sd,
                        hitPct,
                        critPct,
                        missPct,
                        dodgePct,
                        parryPct,
                        glancePct
                    )
                }.sortedBy { it.total }.reversed()

                for(row in rows) {
                    row(row.name, row.count, row.total, row.average, row.median, row.sd, row.hitPct, row.critPct, row.missPct, row.dodgePct, row.parryPct, row.glancePct)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    precision(1, 0)
                    for(i in 2..11) {
                        precision(i, 2)
                    }

                    for(i in 1..11) {
                        formatFlag(i, ",")
                    }

                    for(i in 6..11) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        }
    }
}
