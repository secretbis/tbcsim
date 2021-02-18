package sim.statsmodel

import kotlin.js.JsExport

@JsExport
data class AbilityBreakdown(
    val name: String,
    val countAvg: Double,
    val totalAvg: Double,
    val pctOfTotal: Double,
    val avgHit: Double,
    val avgCrit: Double,
    val hitPct: Double,
    val critPct: Double,
    val missPct: Double,
    val dodgePct: Double,
    val parryPct: Double,
    val glancePct: Double,
)
