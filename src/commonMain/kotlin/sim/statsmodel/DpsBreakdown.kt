package sim.statsmodel

import kotlin.js.JsExport

@JsExport
data class DpsBreakdown(
    val median: Double,
    val mean: Double,
    val sd: Double
)
