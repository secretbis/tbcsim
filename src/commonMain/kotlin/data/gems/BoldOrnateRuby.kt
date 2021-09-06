package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class BoldOrnateRuby : Gem(28362, "Bold Ornate Ruby", "inv_misc_gem_ruby_02.jpg", null, Color.RED, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        attackPower = 20,
        rangedAttackPower = 20
    )
}
