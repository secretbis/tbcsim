package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class BeastLordArmor : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Beast Lord Armor (2 set)"
        const val FOUR_SET_BUFF_NAME = "Beast Lord Armor (4 set)"

        fun twoSetTrapCooldownReductionMs(): Int {
            return 4000
        }
    }

    override val id: Int = 650

    // TODO: Hunter traps should check this buff once they exist
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_helmet_19.jpg"
    }

    // Big yikes on this one blizz
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean =  true

        val armorPenBuff = object : Buff() {
            override val name: String = "$FOUR_SET_BUFF_NAME (Armor Pen)"
            override val icon: String = "inv_helmet_19.jpg"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(armorPen = 600)
            }
        }

        val armorPenProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.HUNTER_CAST_KILL_COMMAND
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(armorPenBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(armorPenProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
