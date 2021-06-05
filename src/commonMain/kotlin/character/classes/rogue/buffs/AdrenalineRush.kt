package character.classes.rogue.buffs

import character.*
import character.classes.rogue.abilities.*
import data.Constants
import sim.Event
import sim.SimParticipant
import data.model.Item
import mechanics.Rating

class AdrenalineRush() : Buff() {
    companion object {
        const val name = "Adrenaline Rush"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 15000

    val extraEnergyPerTick: Int = 20

    val procTick = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SERVER_TICK
        )
        override val type: Type = Type.STATIC
    
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addResource(extraEnergyPerTick, Resource.Type.ENERGY, name)
        }
    }
    
    override fun procs(sp: SimParticipant): List<Proc> = listOf(procTick)
}