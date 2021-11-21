package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class WarbringerBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Warbringer Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Warbringer Battlegear (4 set)"

        fun twoSetWhirlwindCostReduction(): Double {
            return 5.0
        }
    }

    override val id: Int = 655

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_helmet_58.jpg"
        override val durationMs: Int = -1
    }

    val fourSetAbility = object : Ability() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_helmet_58.jpg"
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_helmet_58.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_DODGE,
                Trigger.MELEE_PARRY
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(2, Resource.Type.RAGE, fourSetAbility)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
