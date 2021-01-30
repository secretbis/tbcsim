package sim

import character.Buff
import data.Constants
import de.m3y.kformat.Table
import de.m3y.kformat.table
import mu.KotlinLogging
import java.text.DecimalFormat
import kotlin.math.sqrt

object SimStats {
    val logger = KotlinLogging.logger {}

    data class BuffSegment(
        val startMs: Int,
        val endMs: Int,
        val refreshCount: Int,
        val buff: Buff,
        val stackDurationsMs: List<Pair<Int, Int>>
    ) {
        val durationMs: Int
            get() = endMs - startMs
    }

    data class AbilityBreakdown(
        val name: String,
        val countAvg: Double,
        val totalAvg: Double,
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
        val appliedAvg: Double,
        val refreshedAvg: Double,
        val uptimePct: Double,
        val avgDuration: Double,
        val avgStacks: Double
    )

    data class DamageTypeBreakdown(
        val type: Constants.DamageType,
        val countAvg: Double,
        val totalAvg: Double
    )

    fun sep() {
        println("************************************************************************************")
    }

    val df = DecimalFormat("#,###.##")

    fun median(l: List<Double>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun sd(l: List<Double>, mean: Double) = sqrt(l.map { (it - mean) * (it - mean) }.average())

    fun precombatStats(sim: SimIteration) {
        println(
            "PLAYER STATS\n" +
            table {
                row("Strength:", sim.strength(), "Phys. Hit:", sim.meleeHitPct())
                row("Agility:", sim.agility(), "Phys. Crit:", sim.meleeCritPct())
                row("Intellect:", sim.intellect(), "Phys. Haste:", 1.0 - sim.meleeHasteMultiplier())
                row("Stamina:", sim.stamina(), "Spell Hit:", sim.spellHitPct())
                row("Spirit:", sim.spirit(), "Spell Crit:", sim.spellCritPct())
                row("Armor Pen:", sim.armorPen(), "Spell Haste:", 1.0 - sim.spellHasteMultiplier())
                row("Attack Power", sim.attackPower(), "Expertise:", sim.expertisePct())
                row("R. Attack Power", sim.rangedAttackPower())

                hints {
                    alignment(0, Table.Hints.Alignment.RIGHT)
                    alignment(1, Table.Hints.Alignment.LEFT)
                    alignment(2, Table.Hints.Alignment.RIGHT)
                    alignment(3, Table.Hints.Alignment.LEFT)
                    alignment(4, Table.Hints.Alignment.RIGHT)
                    alignment(5, Table.Hints.Alignment.LEFT)

                    precision(1, 0)
                    precision(3, 2)
                    precision(5, 2)

                    postfix(3, "%")

                    borderStyle = Table.BorderStyle.NONE
                }
            }.render(StringBuilder())
        )

        println(
            "TARGET STATS\n" +
            table {
                row("Arcane Res:", sim.targetStats.arcaneResistance, "Armor:", sim.targetArmor())
                row("Fire Res:", sim.targetStats.fireResistance)
                row("Frost Res:", sim.targetStats.frostResistance)
                row("Nature Res:", sim.targetStats.natureResistance)
                row("Shadow Res:", sim.targetStats.shadowResistance)


                hints {
                    alignment(0, Table.Hints.Alignment.RIGHT)
                    alignment(1, Table.Hints.Alignment.LEFT)
                    alignment(2, Table.Hints.Alignment.RIGHT)
                    alignment(3, Table.Hints.Alignment.LEFT)
                    alignment(4, Table.Hints.Alignment.RIGHT)
                    alignment(5, Table.Hints.Alignment.LEFT)

                    precision(1, 0)

                    borderStyle = Table.BorderStyle.NONE
                }
            }.render(StringBuilder())
        )
    }

    fun dps(iterations: List<SimIteration>) {
        val perIteration = iterations.map {
            it.events.filter { evt -> evt.eventType == Event.Type.DAMAGE }.fold(0.0) { acc, event ->
                acc + event.amount
            } / (it.opts.durationMs / 1000.0)
        }

        val median = median(perIteration)
        val mean = perIteration.average()
        val sd = sd(perIteration, mean)

        sep()
        println("AVERAGE DPS: ${df.format(mean)}")
        println("MEDIAN DPS: ${df.format(median)}")
        println("STDDEV DPS: ${df.format(sd)}")
        sep()
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

        println(
            "$title\n" +
            table {
                header("Name", "AppliedCountAvg", "RefreshedCountAvg", "UptimePct", "AvgDurationSeconds" /*, "AvgStacks" */)

                val rows = keys.map { key ->
                    val events = byBuff[key]!!
                    val applied = events.filter { it.eventType == buffStart }.size / iterations.size.toDouble()
                    val refreshed = events.filter { it.eventType == buffRefresh }.size / iterations.size.toDouble()

                    val segments = mutableListOf<BuffSegment>()
                    var lastEvent: Event? = null
                    var currentStart: Event? = null
                    var refreshCount = 0
                    var stackDurationsMs = mutableListOf<Pair<Int, Int>>()

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
                                segments.add(BuffSegment(currentStart!!.timeMs, it.timeMs, refreshCount, it.buff!!, stackDurationsMs))
                                // Reset
                                currentStart = null
                                refreshCount = 0
                                stackDurationsMs = mutableListOf()
                            } else {
                                logger.warn { "Possibly invalid segment - found end without start for buff: ${it.buff!!.name}" }
                            }
                        }

                        // Track stacks
                        // FIXME: Need more event data about stack consumption in order to actually do this
//                        if(it.eventType == buffRefresh || it.eventType == buffEnd) {
//                            if(it.buffStacks != 0) {
//                                stackDurationsMs.add(Pair(it.buffStacks, it.timeMs - lastEvent!!.timeMs))
//                            }
//                        }

                        lastEvent = it
                    }

                    val uptimePct = segments.map { it.durationMs }.sum() / (iterations.size * iterations[0].opts.durationMs).toDouble() * 100.0
                    val avgDuration = segments.map { it.durationMs }.sum() / segments.size.toDouble() / 1000.0
//                    val avgStacks = segments.map { segment ->
//                        val totalSegmentTimeMs = segment.stackDurationsMs.sumBy { it.second }
//                        segment.stackDurationsMs.sumBy { it.first } / totalSegmentTimeMs.toDouble()
//                    }.sum() / 1000.0

                    BuffBreakdown(
                        key,
                        applied,
                        refreshed,
                        uptimePct,
                        avgDuration,
                        avgStacks = 0.0
                    )
                }.sortedBy { it.name }

                for(row in rows) {
                    row(row.name, row.appliedAvg, row.refreshedAvg, row.uptimePct, row.avgDuration/*, row.avgStacks */)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    for(i in 1..5) {
                        precision(i, 2)
                        formatFlag(i, ",")
                    }

                    for(i in 3..3) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        )
    }

    fun resultsByAbility(iterations: List<SimIteration>) {
        val byAbility = iterations.flatMap { iter ->
            iter.events
                .filter { it.eventType == Event.Type.DAMAGE }
                .filter { it.abilityName != null }
            }
            .groupBy { it.abilityName!! }

        val keys = byAbility.keys.toList()

        println(
            "Ability Breakdown\n" +
            table {
                header("Name", "CountAvg", "TotalDmgAvg", "PctOfTotal", "AverageDmg", "MedianDmg", "Hit%", "Crit%", "Miss%", "Dodge%", "Parry%", "Glance%")

                val rows = keys.map { key ->
                    val events = byAbility[key]!!
                    val amounts = events.map { it.amount }
                    val countAvg = amounts.size.toDouble() / iterations.size.toDouble()
                    val totalAvg = amounts.sum() / iterations.size.toDouble()

                    // Compute damage stats only for events where an attack actually connected
                    val nonzeroAmounts = amounts.filter { it > 0 }
                    val average = amounts.sum() / nonzeroAmounts.size.toDouble()
                    val median = median(nonzeroAmounts)

                    // Compute result distributions with the entire set of events
                    // Count blocked hits/crits as hits/crits, since the block value is very small
                    val hitPct = events.filter { it.result == Event.Result.HIT || it.result == Event.Result.BLOCK }.size / amounts.size.toDouble() * 100.0
                    val critPct = events.filter { it.result == Event.Result.CRIT || it.result == Event.Result.BLOCKED_CRIT }.size / amounts.size.toDouble() * 100.0
                    val missPct = events.filter { it.result == Event.Result.MISS }.size / amounts.size.toDouble() * 100.0
                    val dodgePct = events.filter { it.result == Event.Result.DODGE }.size / amounts.size.toDouble() * 100.0
                    val parryPct = events.filter { it.result == Event.Result.PARRY }.size / amounts.size.toDouble() * 100.0
                    val glancePct = events.filter { it.result == Event.Result.GLANCE }.size / amounts.size.toDouble() * 100.0

                    AbilityBreakdown(
                        key,
                        countAvg,
                        totalAvg,
                        average,
                        median,
                        hitPct,
                        critPct,
                        missPct,
                        dodgePct,
                        parryPct,
                        glancePct
                    )
                }.sortedBy { it.totalAvg }.reversed()

                val grandTotal: Double = rows.sumByDouble { it.totalAvg }

                for(row in rows) {
                    val pctOfGrandTotal = row.totalAvg / grandTotal * 100.0
                    row(row.name, row.countAvg, row.totalAvg, pctOfGrandTotal, row.average, row.median, row.hitPct, row.critPct, row.missPct, row.dodgePct, row.parryPct, row.glancePct)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    for(i in 1..11) {
                        precision(i, 2)
                        formatFlag(i, ",")
                    }

                    postfix(3, "%")
                    for(i in 6..11) {
                        postfix(i, "%")
                    }

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        )
    }

    fun resultsByDamageType(iterations: List<SimIteration>) {
        val byDmgType = iterations.flatMap { iter ->
            iter.events
                .filter { it.eventType == Event.Type.DAMAGE }
                .filter { it.damageType != null }
            }
            .groupBy { it.damageType }

        val keys = byDmgType.keys.toList()

        println(
            "Damage Type Breakdown\n" +
            table {
                header("Name", "CountAvg", "TotalDmgAvg", "PctOfTotal")

                val rows = keys.map { key ->
                    val events = byDmgType[key]!!
                    val amounts = events.map { it.amount }
                    val count = amounts.size.toDouble() / iterations.size.toDouble()
                    val total = amounts.sum() / iterations.size.toDouble()

                    DamageTypeBreakdown(
                        key!!,
                        count,
                        total
                    )
                }.sortedBy { it.totalAvg }.reversed()

                val grandTotal: Double = rows.sumByDouble { it.totalAvg }

                for(row in rows) {
                    val pctOfGrandTotal = row.totalAvg / grandTotal * 100.0
                    row(row.type.name, row.countAvg, row.totalAvg, pctOfGrandTotal)
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)

                    for(i in 1..3) {
                        precision(i, 2)
                    }

                    for(i in 1..11) {
                        formatFlag(i, ",")
                    }

                    postfix(3, "%")

                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render(StringBuilder())
        )
    }
}
