package sim.statsmodel

import character.Buff
import kotlin.js.JsExport

@JsExport
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
