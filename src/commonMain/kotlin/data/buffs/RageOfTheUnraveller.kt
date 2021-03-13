package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class RageOfTheUnraveller : Buff() {
    override val id: Int = 33648
    override val name: String = "Rage of the Unraveller (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PPM
        override val ppm: Double = 1.0

        val buff = object : Buff() {
            override val name: String = "Rage of the Unraveller"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(attackPower = 300)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
