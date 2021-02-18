package sim.statsmodel

import kotlin.js.JsExport

@JsExport
data class DamageTypeBreakdown(
    val type: String,
    val countAvg: Double,
    val totalAvg: Double,
    val pctOfTotal: Double
)
