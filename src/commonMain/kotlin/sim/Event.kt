package sim

import character.Buff
import data.Constants
import character.Resource
import kotlin.js.JsExport

@JsExport
data class Event(
    var tick: Int = -1,
    var timeMs: Int = -1,
    val abilityName: String? = null,
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
    var comboPointsSpent: Int = 0
)
