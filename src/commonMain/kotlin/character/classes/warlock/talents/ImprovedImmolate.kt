package character.classes.warlock.talents

import character.Talent

class ImprovedImmolate(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Immolate"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun immolateInitialDamageMultiplier(): Double {
        return 1.0 + (0.05 * currentRank)
    }
}
