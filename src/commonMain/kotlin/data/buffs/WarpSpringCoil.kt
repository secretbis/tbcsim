package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class WarpSpringCoil : Buff() {
    override val name: String = "Warp-Spring Coil (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        // Per the internet, this should proc off of abilities that do damage and require energy
        override val triggers: List<Trigger> = listOf(
            Trigger.ROGUE_ANY_DAMAGING_SPECIAL
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 25.0
        override fun cooldownMs(sp: SimParticipant): Int = 30000

        val buff = object : Buff() {
            override val id: Int = 37173
            override val name: String = "Warp-Spring Coil"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(
                    armorPen = 1000
                )
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
