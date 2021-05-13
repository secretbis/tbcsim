package character.classes.rogue.talents

import character.Talent

class ImprovedKidneyShot(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Kidney Shot"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    fun damageTakenMultiplier(): Double {
        return 1.0 + (currentRank * 0.03)
    }
}