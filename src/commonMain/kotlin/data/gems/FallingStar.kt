package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class FallingStar : Gem(33135, "Falling Star", "inv_jewelcrafting_empyreansapphire_02.jpg", null, Color.BLUE, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        stamina = 18
    )
}