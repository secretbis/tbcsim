package sim.statsmodel

import kotlin.js.JsExport
import character.*
import sim.SimParticipant

@JsExport
data class ResourceByAbility(
    val name: String,
    val icon: String,
    val countAvg: Double,
    val totalGainAvg: Double,
    val gainPerCountAvg: Double
)
