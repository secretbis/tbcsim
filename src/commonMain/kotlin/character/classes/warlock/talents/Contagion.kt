package character.classes.warlock.talents

import character.Talent

class Contagion(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Contagion"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun additionalDamageMultiplier(): Double {
        return 1.0 + (0.01 * currentRank)
    }
}
