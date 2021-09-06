package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class PotentOrnateTopaz : Gem(28123, "Potent Ornate Topaz", "inv_misc_gem_opal_02.jpg", null, Color.ORANGE, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        spellDamage = 6,
        spellCritRating = 5.0
    )
}
