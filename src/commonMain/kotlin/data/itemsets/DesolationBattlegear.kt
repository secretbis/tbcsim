package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class DesolationBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Desolation Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Desolation Battlegear (4 set)"
    }

    override val id: Int = 660

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(physicalHitRating = 35.0)
        }
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val apBuff = object : Buff() {
            override val name: String = "$FOUR_SET_BUFF_NAME (AP)"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(
                    attackPower = 160,
                    rangedAttackPower = 160
                )
            }
        }

        val apProc = object : Proc() {
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
            override fun percentChance(sp: SimParticipant): Double = 2.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(apBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(apProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
