package character.classes.warlock.talents

import character.Talent

class Cataclysm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Cataclysm"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun destructionCostReduction(): Double {
        return currentRank * 0.01
    }
}
