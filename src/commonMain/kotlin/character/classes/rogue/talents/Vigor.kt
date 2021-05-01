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

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        var energyGiven = false

        override fun refresh(sp: SimParticipant) {
            if(!energyGiven) {
                sp.resources[Resource.Type.ENERGY]!!.maxAmount += maxEnergyIncrease()
                // this should only ever happen at the beginning of the sim, so fill up to max
                sp.resources[Resource.Type.ENERGY]!!.add(10)
                energyGiven = true
            }
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}