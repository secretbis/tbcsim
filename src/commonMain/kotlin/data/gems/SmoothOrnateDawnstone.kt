package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality
import kotlin.js.JsExport

@JsExport
class SmoothOrnateDawnstone : Gem(28119, "Smooth Ornate Dawnstone", "inv_misc_gem_topaz_01.jpg", null, Color.YELLOW, Quality.EPIC) {
    override var phase: Int = 1
    override var stats: Stats = Stats(
        meleeCritRating = 10.0,
        rangedCritRating = 10.0
    )
}
