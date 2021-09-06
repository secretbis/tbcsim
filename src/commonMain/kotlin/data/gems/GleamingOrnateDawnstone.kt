package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class GleamingOrnateDawnstone : Gem(28120, "Gleaming Ornate Dawnstone", "inv_misc_gem_topaz_02.jpg", null, Color.YELLOW, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        spellCritRating = 10.0
    )
}
