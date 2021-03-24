package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class ShiffarsNexusHorn : Buff() {

    override val name: String = "Shiffar's Nexus-Horn (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {

        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_CRIT
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 20.0
        override fun cooldownMs(sp: SimParticipant): Int = 45000
        //Proc rate and ICD from historical wowwiki

        val buff = object : Buff() {
            override val id: Int = 34320
            override val name: String = "Shiffar's Nexus-Horn"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(spellDamage = 225)
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }
    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
