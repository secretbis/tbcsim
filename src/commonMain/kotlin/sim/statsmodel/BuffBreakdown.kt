package sim.statsmodel

import kotlin.js.JsExport

@JsExport
data class BuffBreakdown(
    val name: String,
    val icon: String,
    val appliedAvg: Double,
    val refreshedAvg: Double,
    val uptimePct: Double,
    val avgDuration: Double,
    val avgStacks: Double
)
