package character.classes.rogue.talents

import character.*
import sim.SimParticipant
import data.model.Item
import sim.Event

class Vigor(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Vigor"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    fun maxEnergyIncrease(): Int {
        return currentRank * 10
    }
}