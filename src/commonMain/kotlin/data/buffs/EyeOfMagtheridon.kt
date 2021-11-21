package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class EyeOfMagtheridon : Buff() {

    override val name: String = "Eye of Magtheridon (static)"
    override val icon: String = "inv_elemental_mote_life01.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_RESIST
        )
        override val type: Type = Type.STATIC
        //100% proc rate, only procs from full resists, no ICD

        val buff = object : Buff() {
            override val id: Int = 34749
            override val name: String = "Eye of Magtheridon"
            override val icon: String = "inv_elemental_mote_life01.jpg"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(spellDamage = 170)
            }
        }
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
