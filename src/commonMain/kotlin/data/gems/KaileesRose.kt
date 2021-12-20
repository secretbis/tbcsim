package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class KaileesRose : Gem(33134, "Kailee's Rose", "inv_jewelcrafting_crimsonspinel_02.jpg", null, Color.RED, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        spellHealing = 26,
        spellDamage = 9
    )
}