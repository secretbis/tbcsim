package sim

import character.Ability
import character.Buff
import data.Constants
import character.Resource
import kotlin.js.JsExport

@JsExport
data class Event(
    var tick: Int = -1,
    var timeMs: Int = -1,
    val ability: Ability? = null,
    val buff: Buff? = null,
    val resourceType: Resource.Type? = null,
    val buffStacks: Int = 0,
    val buffCharges: Int = 0,
    val eventType: EventType,
    var target: SimParticipant? = null,
    val damageType: Constants.DamageType? = null,
    val isWhiteDamage: Boolean = false,
    val amount: Double = 0.0,
    val amountPct: Double = 0.0,
    val delta: Double = 0.0,
    val result: EventResult = EventResult.NONE,
    val partialAmount: Double = 0.0,
    var comboPointsSpent: Int = 0,

    // Threat values
    val abilityThreatMultiplier: Double = 1.0,
    // The difference between these is that ability bonus threat is affected by innate threat modifiers
    // while flat bonus threat is not - it is just added, unmodified (e.g. power gains)
    val abilityBonusThreat: Double = 0.0,
    val flatBonusThreat: Double = 0.0
)
