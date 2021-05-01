package character.classes.rogue.talents

import character.Talent

class ImprovedPoisons(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Poisons"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun poisonApplyChancePercent(): Double {
        return currentRank * 2.0
    }
}