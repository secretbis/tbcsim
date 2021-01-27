package sim

import character.Buff
import data.Constants
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
        val hitPct: Double,
        val critPct: Double,
        val missPct: Double,
        val dodgePct: Double,
        val parryPct: Double,
        val glancePct: Double,
    )

    data class BuffBreakdown(
        val name: String,
        val applied: Int,
        val refreshed: Int,
        val uptimePct: Double,
        val avgDuration: Double
    )

    data class DamageTypeBreakdown(
        val type: Constants.DamageType,
        val count: Int,
        val total: Double
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

        logger.info("Average DPS: ${df.format(mean)}")
        logger.info("Median DPS: ${df.format(median)}")
        logger.info("Std. Dev: ${df.format(sd)}")
    }

    fun resultsByBuff(iterations: List<SimIteration>) {
        processBuffs(
            iterations,
            "Buffs",
            iterations.flatMap { iter ->
                iter.events
                    .filter {
                        it.buff != null && !it.buff.hidden && (
                            it.eventType == Event.Type.BUFF_START ||
                            it.eventType == Event.Type.BUFF_REFRESH ||
                            it.eventType == Event.Type.BUFF_END
                        )
                    }
                }
                .groupBy { it.buff!!.name },
            Event.Type.BUFF_START,
            Event.Type.BUFF_REFRESH,
            Event.Type.BUFF_END
        )
    }

    fun resultsByDebuff(iterations: List<SimIteration>) {
        processBuffs(
            iterations,
            "Debuffs",
            iterations.flatMap { iter ->
                iter.events
                    .filter {
                        it.buff != null && !it.buff.hidden && (
                            it.eventType == Event.Type.DEBUFF_START ||
                            it.eventType == Event.Type.DEBUFF_REFRESH ||
                            it.eventType == Event.Type.DEBUFF_END
                        )
                    }
                }
                .groupBy { it.buff!!.name },
            Event.Type.DEBUFF_START,
            Event.Type.DEBUFF_REFRESH,
            Event.Type.DEBUFF_END
        )
    }

    private fun processBuffs(
        iterations: List<SimIteration>,
        title: String,
        byBuff: Map<String, List<Event>>,
        buffStart: Event.Type,
        buffRefresh: Event.Type,
        buffEnd: Event.Type
    ) {
        val keys = byBuff.keys.toList()

        logger.info {
            "$title\n" +
            table {
                header("Name", "AppliedCount", "RefreshedCount", "UptimePct", "AvgDurationSeconds")

                val rows = keys.map { key ->
                    val events = byBuff[key]!!
                    val applied = events.filter { it.eventType == buffStart }.size
                    val refreshed = events.filter { it.eventType == buffRefresh }.size

                    val segments = mutableListOf<BuffSegment>()
                    var lastEvent: Event? = null
                    var currentStart: Event? = null
                    var refreshCount = 0
                    events.forEach {
                        if(it.eventType == buffStart) {
                            if(lastEvent?.eventType == buffStart) {
                                logger.warn { "Possibly invalid segment - found two starts for buff: ${it.buff!!.name}" }
                            }

                            currentStart = it
                        }

                        if(it.eventType == buffRefresh) {
                            if(lastEvent?.eventType == buffEnd) {
                                logger.warn { "Possibly invalid segment - refresh without enclosing start for buff: ${it.buff!!.name}" }
                            }
                            refreshCount++
                        }

                        if(it.eventType == buffEnd) {
                            if(lastEvent?.eventType == buffEnd) {
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

                    val uptimePct = segments.map { it.durationMs }.sum() / (iterations.size * iterations[0].opts.durationMs).toDouble() * 100.0
                    val avgDuration = segments.map { it.durationMs }.sum() / segments.size.toDouble() / 1000.0

                    BuffBreakdown(
                        key,
                        applied,
                        refreshed,
                        uptimePct,
                        avgDuration
                    )
                }.sortedBy { it.name }

                for(row in rows) {
                    row(row.name, row.applied, row.refreshed, row.uptimePct, row.avgDuration)
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
        val byAbility = iterations.flatMap { iter ->
            iter.events
                .filter { it.eventType == Event.Type.DAMAGE }
                .filter { it.abilityName != null }
            }
            .groupBy { it.abilityName!! }

        val keys = byAbility.keys.toList()

        logger.info {
            "Ability Breakdown\n" +
            table {
                header("Name", "Count", "TotalDmg", "PctOfTotal", "AverageDmg", "MedianDmg", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                val rows = keys.map { key ->
                    val events = byAbility[key]!!
                    val amounts = events.map { it.amount }
                    val count = amounts.size.toDouble()
                    val total = amounts.sum()

                    // Compute damage stats only for events where an attack actually connected
                    val nonzeroAmounts = amounts.filter { it > 0 }
                    val average = total / nonzeroAmounts.size.toDouble()
                    val median = median(nonzeroAmounts)

                    // Compute result distributions with the entire set of events
                    // Count blocked hits/crits as hits/crits, since the block value is very small
                    val hitPct = events.filter { it.result == Event.Result.HIT || it.result == Event.Result.BLOCK }.size / count * 100.0
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
                        hitPct,
                        critPct,
                        missPct,
                        dodgePct,
                        parryPct,
                        glancePct
                    )
                }.sortedBy { it.total }.reversed()

                val grandTotal: Double = rows.sumByDouble { it.total }

                for(row in rows) {
                    val pctOfGrandTotal = row.total / grandTotal * 100.0
                    row(row.name, row.count, row.total, pctOfGrandTotal, row.average, row.median, row.hitPct, row.critPct, row.missPct, row.dodgePct, row.parryPct, row.glancePct)
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

                    postfix(3, "%")
                    for(i in 6..11) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        }
    }

    fun resultsByDamageType(iterations: List<SimIteration>) {
        val byDmgType = iterations.flatMap { iter ->
            iter.events
                .filter { it.eventType == Event.Type.DAMAGE }
                .filter { it.damageType != null }
            }
            .groupBy { it.damageType }

        val keys = byDmgType.keys.toList()

        logger.info {
            "Damage Type Breakdown\n" +
            table {
                header("Name", "Count", "TotalDmg", "PctOfTotal")

                val rows = keys.map { key ->
                    val events = byDmgType[key]!!
                    val amounts = events.map { it.amount }
                    val count = amounts.size.toDouble()
                    val total = amounts.sum()

                    DamageTypeBreakdown(
                        key!!,
                        count.toInt(),
                        total
                    )
                }.sortedBy { it.total }.reversed()

                val grandTotal: Double = rows.sumByDouble { it.total }

                for(row in rows) {
                    val pctOfGrandTotal = row.total / grandTotal * 100.0
                    row(row.type.name, row.count, row.total, pctOfGrandTotal)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    precision(1, 0)
                    for(i in 2..3) {
                        precision(i, 2)
                    }

                    for(i in 1..11) {
                        formatFlag(i, ",")
                    }

                    postfix(3, "%")

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        }
    }
}
