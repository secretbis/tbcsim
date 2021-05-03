package sim.statsmodel

import kotlin.js.JsExport
import character.*

@JsExport
data class ResourceByAbility(
    val name: String,
    val countAvg: Double,
    val totalGainAvg: Double,
    val gainPerCountAvg: Double
)
