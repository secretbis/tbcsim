package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class ManaEtchedRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Mana-Etched Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Mana-Etched Regalia (4 set)"
    }
    override val id: Int = 658

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_jewelry_ring_56.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHitRating = 35.0)
        }
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_jewelry_ring_56.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val spellDamageBuff = object : Buff() {
            override val name: String = "$FOUR_SET_BUFF_NAME (Spell Damage)"
            override val icon: String = "inv_jewelry_ring_56.jpg"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(spellDamage = 110)
            }
        }

        val spellDamageProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT,
                Trigger.SPELL_HIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 2.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(spellDamageBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(spellDamageProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
