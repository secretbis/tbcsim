package character.classes.rogue.talents

import character.*

class DeadenedNerves(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Deadened Nerves"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun physicalDamageDecrease(): Double {
        return currentRank * 0.01
    }
}