package sim

import character.Buff
import data.Constants

data class Event(
    var tick: Int = -1,
    var timeMs: Int = -1,
    val abilityName: String? = null,
    val buff: Buff? = null,
    val eventType: Type,
    val damageType: Constants.DamageType? = null,
    val amount: Double = 0.0,
    val result: Result = Result.NONE,
    val partialAmount: Double = 0.0
) {
    enum class Type {
        DAMAGE,
        SPELL_START,
        SPELL_CAST,
        BUFF_START,
        BUFF_REFRESH,
        BUFF_END,
        DEBUFF_START,
        DEBUFF_REFRESH,
        DEBUFF_END,
        PROC
    }

    enum class Result {
        RESIST,
        PARTIAL_RESIST_CRIT,
        PARTIAL_RESIST_HIT,
        MISS,
        DODGE,
        PARRY,
        GLANCE,
        BLOCK,
        BLOCKED_CRIT,
        CRIT,
        CRUSH,
        HIT,
        NONE
    }
}
