package character.classes.shaman.talents

import character.Talent

class Concussion(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Concussion"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun shockAndLightningMultiplier(): Double {
        return 1.0 + (0.01 * currentRank)
    }
}
