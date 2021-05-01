package character.classes.rogue.talents

import character.Talent

class RemorselessAttacks(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Remorseless Attacks"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun critChancePercentIncrease(): Double {
        return currentRank * 20.0
    }

    // TODO: not implemented, not used in a sim anyways
}