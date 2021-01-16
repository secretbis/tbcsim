package sim

import java.util.*

data class Event(
    val time: Date,
    val type: Type,
    val durationMs: Int,
    val amount: Double,
    val result: Result,
    val partialAmount: Double
) {
    enum class Type {
        MELEE_MH,
        MELEE_OH,
        SPELL,
        BUFF,
        PROC
    }

    enum class Result {
        RESIST,
        PARTIAL_RESIST,
        MISS,
        DODGE,
        PARRY,
        GLANCE,
        HIT,
        CRIT
    }
}
