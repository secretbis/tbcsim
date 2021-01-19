package sim

import java.util.*

data class Event(
    var tick: Int = -1,
    val type: Type,
    val durationMs: Int = 0,
    val amount: Double,
    val result: Result,
    val partialAmount: Double = 0.0
) {
    enum class Type {
        MELEE_MH,
        MELEE_OH,
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
        HIT
    }
}
