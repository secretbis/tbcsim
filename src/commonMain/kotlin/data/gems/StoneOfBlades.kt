package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class StoneOfBlades : Gem(33143, "Stone of Blades", "inv_jewelcrafting_lionseye_02.jpg", null, Color.YELLOW, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        meleeCritRating = 12.0,
        rangedCritRating = 12.0
    )
}