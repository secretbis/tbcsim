package sim.statsmodel

import kotlin.js.JsExport

@JsExport
data class ResourceBreakdown(
    val iterationIdx: Int,
    val series: List<Pair<Int, Double>>
)
