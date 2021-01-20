package sim

import character.Ability
import data.Constants

data class Event(
    var tick: Int = -1,
    val ability: Ability,
    val eventType: Type,
    val damageType: Constants.DamageType? = null,
    val durationMs: Int = 0,
    val amount: Double = 0.0,
    val result: Result = Result.NONE,
    val partialAmount: Double = 0.0
) {
    enum class Type {
        DAMAGE,
        SPELL_START,
        SPELL_CAST,
        BUFF_START,
        BUFF_END,
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
        CRIT,
        CRUSH,
        HIT,
        NONE
    }
}
