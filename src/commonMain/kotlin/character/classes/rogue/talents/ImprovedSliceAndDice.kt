package character.classes.rogue.talents

import character.Talent

class ImprovedSliceAndDice(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Slice and Dice"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun durationMultiplier(): Double {
        return 1.0 + (currentRank * 0.15)
    }
}