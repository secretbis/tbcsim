package character.classes.hunter.talents

import character.Talent

class UnleashedFury(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Unleashed Fury"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun petDamageMultiplier(): Double = 1.0 + (currentRank * 0.04)
}
