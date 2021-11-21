package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class DestroyerBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Destroyer Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Destroyer Battlegear (4 set)"

        fun fourSetBTMSCostReduction(): Double {
            return 5.0
        }
    }
    override val id: Int = 657

    // TODO: Overpower should check this buff if once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_shoulder_29.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val overpowerApBuff = object : Buff() {
            override val name: String = "$TWO_SET_BUFF_NAME (AP)"
            override val icon: String = "inv_shoulder_29.jpg"
            override val durationMs: Int = 5000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(attackPower = 100)
            }
        }

        val overpowerProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.WARRIOR_CAST_OVERPOWER
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(overpowerApBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(overpowerProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_shoulder_29.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
