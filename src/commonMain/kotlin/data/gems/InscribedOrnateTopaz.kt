package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class InscribedOrnateTopaz : Gem(28363, "Inscribed Ornate Topaz", "inv_misc_gem_opal_01.jpg", null, Color.ORANGE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        attackPower = 10,
        rangedAttackPower = 10,
        meleeCritRating = 5.0,
        rangedCritRating = 5.0
    )
}
