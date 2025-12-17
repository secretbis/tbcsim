package character.classes.shaman.talents

import character.Talent

class Convection(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Convection"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun lightningAndShockCostReduction(): Double {
        return currentRank * 0.02
    }
}
