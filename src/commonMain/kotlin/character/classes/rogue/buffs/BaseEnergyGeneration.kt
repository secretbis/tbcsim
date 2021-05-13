package character.classes.rogue.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration
import sim.SimParticipant

class BaseEnergyGeneneration : Buff() {
    override val name: String = "Base Energy Generation"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val energyPerTick = 20

    val procTick = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SERVER_TICK
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addResource(energyPerTick, Resource.Type.ENERGY, name)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(procTick)
}
