package character.classes.warlock.talents

import character.Talent

class ImprovedCurseOfAgony(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Curse of Agony"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun damageMultiplier(): Double {
        return 1.0 + (0.1 * currentRank)
    }
}
