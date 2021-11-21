package sim

import mu.KotlinLogging
import sim.statsmodel.*
import kotlin.js.JsExport
import kotlin.math.sqrt
import kotlin.random.Random
import character.*
import data.Constants

@JsExport
object SimStats {
    val logger = KotlinLogging.logger {}

    fun sep() {
        println("************************************************************************************")
    }

    fun median(l: List<Double>) = l.sorted().let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
    fun sd(l: List<Double>, mean: Double) = sqrt(l.map { (it - mean) * (it - mean) }.average())

    private fun dpsForParticipant(sp: SimParticipant): Double {
        return sp.events.filter { evt -> evt.eventType == EventType.DAMAGE }.fold(0.0) { acc, event ->
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
            EventType.BUFF_START,
            EventType.BUFF_REFRESH,
            EventType.BUFF_END
        )
    }

    fun resultsByDebuff(iterations: List<SimIteration>): List<List<BuffBreakdown>> {
        return processBuffs(
            iterations,
            EventType.DEBUFF_START,
            EventType.DEBUFF_REFRESH,
            EventType.DEBUFF_END,
            onlyTarget = true
        )
    }

    private fun processBuffs(
        iterations: List<SimIteration>,
        buffStart: EventType,
        buffRefresh: EventType,
        buffEnd: EventType,
        onlyTarget: Boolean = false
    ): List<List<BuffBreakdown>> {
        val eventTypes = listOf(buffStart, buffRefresh, buffEnd)
        val showHidden = iterations[0].opts.showHiddenBuffs
        val participantCount = if(onlyTarget) { 0 } else (iterations[0].participants.size - 1)

        // Aggregate all events for each participant across all iterations
        return (0..participantCount).map { idx ->
            val allBuffs: MutableMap<String, Buff> = mutableMapOf()
            val byBuff = iterations.flatMap { iter ->
                val context = if(onlyTarget) { iter.target } else iter.participants[idx]
                context.events
                    .filter { it.buff != null && (!it.buff.hidden || showHidden) && eventTypes.contains(it.eventType) }
                    .filter { it.buff?.name != null }
            }.also {
                // Store a buff reference so we can lookup members later
                // Using the Buff object as the key does not translate well to JS
                for(evt in it) {
                    allBuffs[evt.buff!!.name] = evt.buff
                }
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
                    allBuffs[key]?.icon ?: Constants.UNKNOWN_ICON,
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
            val allAbilities: MutableMap<String, Ability> = mutableMapOf()
            val byAbility = iterations.flatMap { iter ->
                iter.participants[idx].events
                    .filter { it.eventType == EventType.DAMAGE }
                    .filter { it.ability?.name != null }
            }.also {
                // Store an ability reference so we can lookup members later
                // Using the Ability object as the key does not translate well to JS
                for(evt in it) {
                    allAbilities[evt.ability!!.name] = evt.ability
                }
            }.groupBy { it.ability!!.name }

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
                    events.filter { it.result == EventResult.HIT || it.result == EventResult.BLOCK || it.result == EventResult.PARTIAL_RESIST_HIT }
                val allCrits =
                    events.filter { it.result == EventResult.CRIT || it.result == EventResult.BLOCKED_CRIT || it.result == EventResult.PARTIAL_RESIST_CRIT }
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
                    events.filter { it.result == EventResult.MISS || it.result == EventResult.RESIST }.size / amounts.size.toDouble() * 100.0
                val dodgePct = events.filter { it.result == EventResult.DODGE }.size / amounts.size.toDouble() * 100.0
                val parryPct = events.filter { it.result == EventResult.PARRY }.size / amounts.size.toDouble() * 100.0
                val glancePct =
                    events.filter { it.result == EventResult.GLANCE }.size / amounts.size.toDouble() * 100.0

                val pctOfTotal = totalAvg / grandTotal * 100.0

                AbilityBreakdown(
                    key,
                    allAbilities[key]?.icon ?: Constants.UNKNOWN_ICON,
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
                    .filter { it.eventType == EventType.DAMAGE }
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

    fun resourceUsage(iterations: List<SimIteration>): List<Map<String, ResourceBreakdown>> {
        // Pick an execution at random
        // TODO: Average and +/- some number of stddevs usage across iterations for each participant
        //       Multiple lines for avg, percentiles?
        val iterationIdx = Random.nextInt(iterations.size)
        val participants = iterations[iterationIdx].participants

        return participants.mapIndexed { _, sp ->
            val resourceTypes = sp.character.klass.resourceTypes

            resourceTypes.fold(mutableMapOf()) { acc, resourceType ->
                val series =
                    sp.events.filter { it.eventType == EventType.RESOURCE_CHANGED && it.resourceType == resourceType }
                        .map {
                            Pair((it.timeMs / 1000.0).toInt(), it.amountPct)
                        }

                acc[resourceType.name] = ResourceBreakdown(
                    iterationIdx,
                    series
                )
                acc
            }
        }
    }

    fun resourceUsageByAbility(iterations: List<SimIteration>): List<Map<String, List<ResourceByAbility>>> {
        val participantCount = iterations[0].participants.size - 1
        return (0..participantCount).map { idx ->
            val sp = iterations[0].participants[idx]
            val resourceTypes = sp.character.klass.resourceTypes

            // Also group by resource type
            resourceTypes.fold(mutableMapOf()) { acc, resourceType ->
                val allAbilities: MutableMap<String, Ability> = mutableMapOf()
                val byAbility = iterations.flatMap { iter ->
                    iter.participants[idx].events
                        .filter { it.eventType == EventType.RESOURCE_CHANGED }
                        .filter { it.ability?.name != null }
                        .filter { it.resourceType == resourceType }
                }.also {
                    // Store an ability reference so we can lookup members later
                    // Using the Ability object as the key does not translate well to JS
                    for(evt in it) {
                        allAbilities[evt.ability!!.name] = evt.ability
                    }
                }.groupBy { it.ability!!.name }

                val keys = byAbility.keys.toList()

                acc[resourceType.name] = keys.map { key ->
                    val events = byAbility[key]!!
                    val deltas = events.map { it.delta }
                    val countAvg = deltas.size.toDouble() / iterations.size.toDouble()
                    val totalGainAvg = deltas.sum() / iterations.size.toDouble()

                    ResourceByAbility(
                        key,
                        allAbilities[key]?.icon ?: Constants.UNKNOWN_ICON,
                        countAvg,
                        totalGainAvg,
                        totalGainAvg / countAvg
                    )
                }.sortedBy { it.totalGainAvg }.reversed()
                acc
            }
        }
    }
}
