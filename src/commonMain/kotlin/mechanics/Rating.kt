package mechanics

import kotlin.js.JsExport

@JsExport
object Rating {
    // At level 70
    const val physicalHitPerPct: Double = 15.77
    const val spellHitPerPct: Double = 12.62
    const val expertisePerPct: Double = 15.77
    const val expertiseRatingPerPoint: Double = 3.9423
    const val critPerPct: Double = 22.08
    const val hastePerPct: Double = 15.77
    const val dodgePerPct: Double = 18.92
    const val parryPerPct: Double = 23.65
    const val blockPerPct: Double = 7.88
    const val defensePerPoint: Double = 2.36
    const val resiliencePerPct: Double = 14.652
}
