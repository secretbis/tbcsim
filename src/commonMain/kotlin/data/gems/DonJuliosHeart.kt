package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class DonJuliosHeart : Gem(33133, "Don Julio's Heart", "inv_jewelcrafting_crimsonspinel_02.jpg", null, Color.RED, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        spellDamage = 14
    )
}