package character.classes.warlock.talents

import character.Talent

class ImprovedLifeTap(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Life Tap"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun lifeTapManaMultiplier(): Double {
        return 1.0 + (0.1 * currentRank)
    }
}
