package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class FacetOfEternity : Gem(33140, "Facet of Eternity", "inv_jewelcrafting_lionseye_02.jpg", null, Color.YELLOW, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        defenseRating = 12.0
    )
}