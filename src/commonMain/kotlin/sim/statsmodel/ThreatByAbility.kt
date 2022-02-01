package sim.statsmodel

import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
data class ThreatByAbility(
    val name: String,
    val icon: String,
    val countAvg: Double,
    val threatPerSecondAvg: Double,
    val threatPerCastAvg: Double
)
