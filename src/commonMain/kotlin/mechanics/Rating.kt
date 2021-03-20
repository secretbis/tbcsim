package mechanics

import kotlin.js.JsExport

@JsExport
object Rating {
    // At level 70
    val meleeHitPerPct: Double = 15.77
    val spellHitPerPct: Double = 12.62
    val expertisePerPct: Double = 15.77
    val expertiseRatingPerPoint: Double = 3.9423
    val critPerPct: Double = 22.08
    val hastePerPct: Double = 15.77
    val dodgePerPct: Double = 18.92
    val parryPerPct: Double = 23.65
    val blockPerPct: Double = 7.88
    val defensePerPoint: Double = 2.36
    val resiliencePerPct: Double = 14.652
}
