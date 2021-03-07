package character.classes.warlock.talents

import character.Talent

class EmpoweredCorruption(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Empowered Corruption"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun corruptionSpellDamageMultiplier(): Double {
        return 1.0 + (0.12 * currentRank)
    }
}
