package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class TirisfalRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Tirisfal Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Tirisfal Regalia (4 set)"

        fun twoSetArcaneBlastDamageAndCostMultiplier(): Double {
            return 1.2
        }
    }

    override val id: Int = 649

    // TODO: Arcane Blast should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_pants_cloth_05.jpg"
        override val durationMs: Int = -1
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_pants_cloth_05.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean =  true

        val spellDamageBuff = object : Buff() {
            override val name: String = "$FOUR_SET_BUFF_NAME (Spell Damage)"
            override val icon: String = "inv_pants_cloth_05.jpg"
            override val durationMs: Int = 6000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(spellDamage = 70)
            }
        }

        val critProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(spellDamageBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(critProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
