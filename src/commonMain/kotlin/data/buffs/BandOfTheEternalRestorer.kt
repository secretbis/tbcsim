package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class BandOfTheEternalRestorer : Buff() {
    companion object {
        const val name = "Band of the Eternal Restorer"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val icon: String = "inv_jewelry_ring_55.jpg"

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_HIT,
            Trigger.SPELL_CRIT,
            Trigger.HEAL_HIT,
            Trigger.HEAL_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0
        override fun cooldownMs(sp: SimParticipant): Int = 60000

        val buff = object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = 10000
            override val icon: String = "inv_jewelry_ring_55.jpg"

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    spellHealing = 175,
                    spellDamage = 59
                )
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }

    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}