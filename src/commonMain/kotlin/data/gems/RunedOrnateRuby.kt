package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class RunedOrnateRuby : Gem(28118, "Runed Ornate Ruby", "inv_misc_gem_bloodstone_01.jpg", null, Color.RED, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        spellDamage = 12
    )
}
