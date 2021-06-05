package character.classes.mage.talents

import character.*
import data.model.Item
import sim.Event
import sim.EventResult
import sim.SimParticipant

class MasterOfElements(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Master of Elements"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.FIRE_DAMAGE_NON_PERIODIC,
                Trigger.FROST_DAMAGE
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                if(event?.result == EventResult.CRIT) {
                    val resourceCost = ability?.resourceCost(sp)
                    if(resourceCost != null) {
                        sp.addResource((resourceCost * 0.1 * currentRank).toInt(), Resource.Type.MANA, Companion.name)
                    }
                }
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
