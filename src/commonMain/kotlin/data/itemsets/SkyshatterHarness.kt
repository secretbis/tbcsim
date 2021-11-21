package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class SkyshatterHarness : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Skyshatter Harness (2 set)"
        const val FOUR_SET_BUFF_NAME = "Skyshatter Harness (4 set)"

        fun twoSetShockCostReductionPct(): Double {
            return 0.10
        }

    }
    override val id: Int = 682

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_shoulder_61.jpg"
        override val durationMs: Int = -1
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_shoulder_61.jpg"
        override val durationMs: Int = -1

        val apBuff = object : Buff() {
            override val name: String = "$FOUR_SET_BUFF_NAME (AP)"
            override val icon: String = "inv_shoulder_61.jpg"
            override val durationMs: Int = 12000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(attackPower = 70)
            }
        }

        val fourProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHAMAN_CAST_STORMSTRIKE
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(apBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(fourProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
