package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BandOfTheEternalDefender : Buff() {
    companion object {
        const val name = "Band of the Eternal Defender"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "inv_jewelry_ring_55.jpg"

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_HIT,
            Trigger.INCOMING_MELEE_CRIT,
            Trigger.INCOMING_MELEE_CRUSH,
            Trigger.INCOMING_MELEE_BLOCK
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 3.0
        override fun cooldownMs(sp: SimParticipant): Int = 60000

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 10000
            override val icon: String = "inv_jewelry_ring_55.jpg"

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(armor = 800)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }

    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}