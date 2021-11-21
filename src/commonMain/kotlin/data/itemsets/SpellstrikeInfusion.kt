package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class SpellstrikeInfusion : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Spellstrike Infusion (2 set)"
    }
    override val id: Int = 559

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_pants_cloth_14.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val blastingBuff = object : Buff() {
            override val name: String = "$TWO_SET_BUFF_NAME (Spell Damage)"
            override val icon: String = "inv_pants_cloth_14.jpg"
            override val durationMs: Int = 10000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(spellDamage = 92)
            }
        }

        val blastingProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_HIT,
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 5.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(blastingBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(blastingProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff)
    )
}
