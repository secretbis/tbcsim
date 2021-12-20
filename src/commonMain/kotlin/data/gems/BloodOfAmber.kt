package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class BloodOfAmber : Gem(33140, "Blood of Amber", "inv_jewelcrafting_lionseye_02.jpg", null, Color.YELLOW, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        spellCritRating = 12.0
    )
}