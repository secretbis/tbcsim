package mechanics

import kotlin.js.JsExport

@JsExport
object General {
    // This takes a list of *reductions* not multipliers, i.e. if a spell says reduced by 60%, send 0.6, not 0.4
    fun resourceCostReduction(baseCost: Double, reductions: List<Double>) : Double {
        // Resource reductions all work relative to the base mana cost, so each needs to be subtracted individually
        return reductions.fold(baseCost) { acc, reduction ->
            acc - acc * reduction
        }.coerceAtLeast(0.0)
    }
}
