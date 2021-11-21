package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class LightbringerBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Lightbringer Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Lightbringer Battlegear (4 set)"

        // TODO: Hammer of Wrath should check this buff once it exists
        fun fourSetHoWDamageMultiplier(): Double {
            return 1.1
        }
    }
    override val id: Int = 680

    val twoSetAbility = object : Ability() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_helmet_96.jpg"
    }

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_helmet_96.jpg"
        override val durationMs: Int = -1

        val manaProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_BLOCK,
                Trigger.MELEE_GLANCE,
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 20.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(50, Resource.Type.MANA, twoSetAbility)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(manaProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_helmet_96.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
