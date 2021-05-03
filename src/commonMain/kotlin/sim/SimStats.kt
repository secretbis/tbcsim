package sim

import mu.KotlinLogging
import sim.statsmodel.*
import kotlin.js.JsExport
import kotlin.math.sqrt
import kotlin.random.Random
import character.*

@JsExport
object SimStats {
    val logger = KotlinLogging.logger {}

    fun sep() {
        println("************************************************************************************")
    }

    fun median(l: List<Double>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun sd(l: List<Double>, mean: Double) = sqrt(l.map { (it - mean) * (it - mean) }.average())

    private fun dpsForParticipant(sp: SimParticipant): Double {
        return sp.events.filter { evt -> evt.eventType == Event.Type.DAMAGE }.fold(0.0) { acc, event ->
            acc + event.amount
        } / (sp.sim.opts.durationMs / 1000.0)
    }

    fun dps(iterations: List<SimIteration>): Map<String, DpsBreakdown?> {
        val hasPet = iterations[0].subject.pet != null
        val perIteration = iterations.map {
            mapOf(
                "subject" to dpsForParticipant(it.subject),
                "subjectPet" to if(hasPet) { dpsForParticipant(it.subject.pet!!) } else null
            )
        }

        val subjectData = perIteration.map { it["subject"]!! }
        val subjectPetData = if(hasPet) { perIteration.map { it["subjectPet"]!! } } else null

        val subjectMean = subjectData.average()
        val subjectPetMean = subjectPetData?.average()
        return mapOf(
            "subject" to DpsBreakdown(
                median(subjectData),
                mean = subjectMean,
                sd = sd(subjectData, subjectMean)
            ),
            "subjectPet" to if(hasPet) {
                DpsBreakdown(
                    median(subjectPetData!!),
                    mean = subjectPetMean!!,
                    sd = sd(subjectPetData, subjectPetMean)
                )
            } else null
        )
    }

    fun resultsByBuff(iterations: List<SimIteration>): List<List<BuffBreakdown>> {
        return processBuffs(
            iterations,
            Event.Type.BUFF_START,
            Event.Type.BUFF_REFRESH,
            Event.Type.BUFF_END
        )
    }

    fun resultsByDebuff(iterations: List<SimIteration>): List<List<BuffBreakdown>> {
        return processBuffs(
            iterations,
            Event.Type.DEBUFF_START,
            Event.Type.DEBUFF_REFRESH,
            Event.Type.DEBUFF_END,
            onlyTarget = true
        )
    }

    private fun processBuffs(
        iterations: List<SimIteration>,
        buffStart: Event.Type,
        buffRefresh: Event.Type,
        buffEnd: Event.Type,
        onlyTarget: Boolean = false
    ): List<List<BuffBreakdown>> {
        val eventTypes = listOf(buffStart, buffRefresh, buffEnd)
        val showHidden = iterations[0].opts.showHiddenBuffs
        val participantCount = if(onlyTarget) { 0 } else (iterations[0].participants.size - 1)

        // Aggregate all events for each participant across all iterations
        return (0..participantCount).map { idx ->
            val byBuff = iterations.flatMap { iter ->
                val context = if(onlyTarget) { iter.target } else iter.participants[idx]
                context.events
                    .filter { it.buff != null && (!it.buff.hidden || showHidden) && eventTypes.contains(it.eventType) }
                    .filter { it.buff?.name != null }
            }.groupBy { it.buff!!.name }

            val keys = byBuff.keys.toList()

            keys.map { key ->
                val events = byBuff[key]!!
                val applied = events.filter { it.eventType == buffStart }.size / iterations.size.toDouble()
                val refreshed = events.filter { it.eventType == buffRefresh }.size / iterations.size.toDouble()

                // General segment tracking
                val segments = mutableListOf<BuffSegment>()
                var lastEvent: Event? = null
                var currentStart: Event? = null
                var refreshCount = 0

                // Stack tracking - this is different because we need the size of each sub-segment
                // start -> <subsegment 1> -> refresh -> <subsegment 2> -> refresh -> <subsegment 3> -> end
                // Pair is time, stack count
                var stackDurationsMs = mutableListOf<Pair<Int, Int>>()
                var lastStackSegmentEvent: Event? = null

                events.forEach {
                    if (it.eventType == buffStart) {
                        if (lastEvent?.eventType == buffStart) {
                            logger.warn { "Possibly invalid segment - found two starts for buff: ${it.buff!!.name}" }
                        }

                        currentStart = it

                        if (it.buffStacks != 0) {
                            lastStackSegmentEvent = it
                        }
                    }

                    if (it.eventType == buffRefresh || it.eventType == buffEnd) {
                        if (it.buffStacks != 0) {
                            if (lastStackSegmentEvent != null) {
                                stackDurationsMs.add(
                                    Pair(
                                        it.timeMs - lastStackSegmentEvent!!.timeMs,
                                        lastStackSegmentEvent!!.buffStacks
                                    )
                                )

                                if (it.eventType == buffRefresh) {
                                    lastStackSegmentEvent = it
                                } else {
                                    lastStackSegmentEvent = null
                                }
                            } else {
                                logger.warn { "Possibly invalid stack segment - refresh without prior start for buff: ${it.buff!!.name}" }
                            }
                        }
                    }

                    if (it.eventType == buffRefresh) {
                        if (lastEvent?.eventType == buffEnd) {
                            logger.warn { "Possibly invalid segment - refresh without prior start for buff: ${it.buff!!.name}" }
                        }
                        refreshCount++
                    }

                    if (it.eventType == buffEnd) {
                        if (lastEvent?.eventType == buffEnd) {
                            logger.warn { "Possibly invalid segment - found two ends for buff: ${it.buff!!.name}" }
                        }

                        if (currentStart != null) {
                            segments.add(
                                BuffSegment(
                                    currentStart!!.timeMs,
                                    it.timeMs,
                                    refreshCount,
                                    it.buff!!,
                                    stackDurationsMs
                                )
                            )
                            // Reset
                            currentStart = null
                            refreshCount = 0
                            stackDurationsMs = mutableListOf()
                        } else {
                            logger.warn { "Possibly invalid segment - found end without start for buff: ${it.buff!!.name}" }
                        }
                    }

                    lastEvent = it
                }

                val uptimePct = segments.map { it.durationMs }
                    .sum() / (iterations.size * iterations[0].opts.durationMs).toDouble() * 100.0
                val avgDuration = segments.map { it.durationMs }.sum() / segments.size.toDouble() / 1000.0
                val avgStacks = segments.map { segment ->
                    // Compute the weighted average of each stack sub-segment
                    if (segment.stackDurationsMs.isNotEmpty()) {
                        val totalSegmentTimeMs = segment.stackDurationsMs.sumBy { it.first }
                        segment.stackDurationsMs.sumBy { it.first * it.second } / totalSegmentTimeMs.toDouble()
                    } else {
                        0.0
                    }
                }.sum() / segments.size.toDouble()

                BuffBreakdown(
                    key,
                    applied,
                    refreshed,
                    uptimePct,
                    avgDuration,
                    avgStacks
                )
            }.sortedBy { it.name }
        }
    }

    fun resultsByAbility(iterations: List<SimIteration>): List<List<AbilityBreakdown>> {
        val participantCount = iterations[0].participants.size - 1

        // Aggregate all events for each participant across all iterations
        return (0..participantCount).map { idx ->
            val byAbility = iterations.flatMap { iter ->
                iter.participants[idx].events
                    .filter { it.eventType == Event.Type.DAMAGE }
                    .filter { it.abilityName != null }
            }.groupBy { it.abilityName!! }

            val keys = byAbility.keys.toList()
            val grandTotal = keys.fold(0.0) { acc, it ->
                acc + (byAbility[it]?.sumByDouble { it.amount } ?: 0.0)
            } / iterations.size.toDouble()

            keys.map { key ->
                val events = byAbility[key]!!
                val amounts = events.map { it.amount }
                val countAvg = amounts.size.toDouble() / iterations.size.toDouble()
                val totalAvg = amounts.sum() / iterations.size.toDouble()

                val allHits =
                    events.filter { it.result == Event.Result.HIT || it.result == Event.Result.BLOCK || it.result == Event.Result.PARTIAL_RESIST_HIT }
                val allCrits =
                    events.filter { it.result == Event.Result.CRIT || it.result == Event.Result.BLOCKED_CRIT || it.result == Event.Result.PARTIAL_RESIST_CRIT }
                val avgHit = allHits.map { it.amount }.sum() / allHits.size.toDouble()
                val minHit = allHits.map { it.amount }.minOrNull() ?: Double.NaN
                val maxHit = allHits.map { it.amount }.maxOrNull() ?: Double.NaN
                val avgCrit = allCrits.map { it.amount }.sum() / allCrits.size.toDouble()
                val minCrit = allCrits.map { it.amount }.minOrNull() ?: Double.NaN
                val maxCrit = allCrits.map { it.amount }.maxOrNull() ?: Double.NaN

                // Compute result distributions with the entire set of events
                // Count blocked hits/crits as hits/crits, since the block value is very small
                val hitPct = allHits.size / amounts.size.toDouble() * 100.0
                val critPct = allCrits.size / amounts.size.toDouble() * 100.0
                val missPct =
                    events.filter { it.result == Event.Result.MISS || it.result == Event.Result.RESIST }.size / amounts.size.toDouble() * 100.0
                val dodgePct = events.filter { it.result == Event.Result.DODGE }.size / amounts.size.toDouble() * 100.0
                val parryPct = events.filter { it.result == Event.Result.PARRY }.size / amounts.size.toDouble() * 100.0
                val glancePct =
                    events.filter { it.result == Event.Result.GLANCE }.size / amounts.size.toDouble() * 100.0

                val pctOfTotal = totalAvg / grandTotal * 100.0

                AbilityBreakdown(
                    key,
                    countAvg,
                    totalAvg,
                    pctOfTotal,
                    minHit,
                    avgHit,
                    maxHit,
                    minCrit,
                    avgCrit,
                    maxCrit,
                    hitPct,
                    critPct,
                    missPct,
                    dodgePct,
                    parryPct,
                    glancePct
                )
            }.sortedBy { it.totalAvg }.reversed()
        }
    }

    fun resultsByDamageType(iterations: List<SimIteration>): List<List<DamageTypeBreakdown>> {
        val participantCount = iterations[0].participants.size - 1
        return (0..participantCount).map { idx ->
            val byDmgType = iterations.flatMap { iter ->
                iter.participants[idx].events
                    .filter { it.eventType == Event.Type.DAMAGE }
                    .filter { it.damageType != null }
            }.groupBy { it.damageType!! }

            val keys = byDmgType.keys.toList()

            val grandTotal = keys.fold(0.0) { acc, it ->
                acc + (byDmgType[it]?.sumByDouble { it.amount } ?: 0.0)
            } / iterations.size.toDouble()

            keys.map { key ->
                val events = byDmgType[key]!!
                val amounts = events.map { it.amount }
                val count = amounts.size.toDouble() / iterations.size.toDouble()
                val total = amounts.sum() / iterations.size.toDouble()
                val pctOfTotal = total / grandTotal * 100.0

                DamageTypeBreakdown(
                    key.name,
                    count,
                    total,
                    pctOfTotal
                )
            }.sortedBy { it.totalAvg }.reversed()
        }
    }

    fun resourceUsage(iterations: List<SimIteration>, resourceType: Resource.Type): List<ResourceBreakdown> {
        // Pick an execution at random
        // TODO: Average and +/- some number of stddevs usage across iterations for each participant
        //       Multiple lines for avg, percentiles?
        val iterationIdx = Random.nextInt(iterations.size)
        val participants = iterations[iterationIdx].participants

        return participants.mapIndexed { idx, sp ->
            val series = sp.events.filter { it.eventType == Event.Type.RESOURCE_CHANGED && it.resourceType == resourceType }.map {
                Pair((it.timeMs / 1000.0).toInt(), it.amountPct)
            }

            ResourceBreakdown(
                iterationIdx,
                series
            )
        }
    }

    fun resourceUsageByAbility(iterations: List<SimIteration>, resourceType: Resource.Type): List<List<ResourceByAbility>> {
        val participantCount = iterations[0].participants.size - 1
        return (0..participantCount).map { idx ->
            val byAbility = iterations.flatMap { iter ->
                iter.participants[idx].events
                    .filter { it.eventType == Event.Type.RESOURCE_CHANGED }
                    .filter { it.abilityName != null }
                    .filter { it.resourceType == resourceType }
            }.groupBy { it.abilityName!! }

            val keys = byAbility.keys.toList()

            keys.map { key ->
                val events = byAbility[key]!!
                val deltas = events.map { it.delta }
                val countAvg = deltas.size.toDouble() / iterations.size.toDouble()
                val totalGainAvg = deltas.sum() / iterations.size.toDouble()

                ResourceByAbility(
                    key,
                    countAvg,
                    totalGainAvg,
                    totalGainAvg / countAvg
                )
            }.sortedBy { it.totalGainAvg }.reversed()
        }
    }
}
