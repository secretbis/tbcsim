package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BulwarkOfAzzinoth : Buff() {
    companion object {
        const val name = "Bulwark of Azzinoth"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "inv_shield_32.jpg"

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_HIT,
            Trigger.INCOMING_MELEE_CRIT,
            Trigger.INCOMING_MELEE_CRUSH,
            Trigger.INCOMING_MELEE_BLOCK
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 2.0

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 10000
            override val icon: String = "inv_shield_32.jpg"

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(armor = 2000)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}