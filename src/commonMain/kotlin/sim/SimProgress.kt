package sim

import kotlin.js.JsExport

@JsExport
data class SimProgress(
    val opts: SimOptions,
    val iterationsCompleted: Int,
    val currentIteration: SimIteration
)
