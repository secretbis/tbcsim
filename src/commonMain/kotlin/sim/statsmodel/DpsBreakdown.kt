package sim.statsmodel

import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
data class DpsBreakdown(
    val median: Double,
    val mean: Double,
    val sd: Double
)
