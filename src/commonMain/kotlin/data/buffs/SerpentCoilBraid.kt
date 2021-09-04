package data.buffs

import character.*
import data.model.Item
import sim.Event
import sim.EventType
import sim.SimParticipant

class SerpentCoilBraid : Buff() {
    companion object {
        const val name: String = "Serpent-Coil Braid (static)"
    }

    override val name: String = Companion.name
    override val id: Int = 37447
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MAGE_MANA_GEM
        )
        override val type: Type = Type.STATIC

        val spBuff = object : Buff() {
            override val id: Int = 37447
            override val name: String = "Serpent-Coil Braid"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(spellDamage = 225)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if(event?.eventType == EventType.RESOURCE_CHANGED) {
                // Add an additional 25% resource
                sp.addResource((event.amount * 0.25).toInt(), Resource.Type.MANA, "Serpent-Coil Braid")
            }

            sp.addBuff(spBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
