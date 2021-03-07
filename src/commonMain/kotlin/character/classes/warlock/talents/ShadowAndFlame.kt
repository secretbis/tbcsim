package character.classes.warlock.talents

import character.Talent

class ShadowAndFlame(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow and Flame"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun bonusDestructionSpellDamageMultiplier(): Double {
        return 1.0 + (0.04 * currentRank)
    }
}
