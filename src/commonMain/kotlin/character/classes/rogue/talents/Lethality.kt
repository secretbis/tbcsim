package character.classes.rogue.talents

import character.Talent

class Lethality(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lethality"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun critDamageMultiplier(): Double {
        return 1.0 + (currentRank * 0.06)
    }
}