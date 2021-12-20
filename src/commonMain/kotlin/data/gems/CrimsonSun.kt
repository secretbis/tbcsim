package data.gems

import character.Stats
import data.model.Color
import data.model.Gem
import data.model.Quality

class CrimsonSun : Gem(33131, "Crimson Sun", "inv_jewelcrafting_crimsonspinel_02.jpg", null, Color.RED, Quality.EPIC) {
    override var phase: Int = 2
    override var stats: Stats = Stats(
        attackPower = 24,
        rangedAttackPower = 24
    )
}