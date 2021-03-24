package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class TheTwinBladesOfAzzinoth : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "The Twin Blades of Azzinoth"
    }
    override val id: Int = 699

    val twoBuffHaste = object : Buff() {
        override val name: String = "$TWO_SET_BUFF_NAME (Haste) (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PPM
            override val ppm: Double = 2.0
            override fun cooldownMs(sp: SimParticipant): Int = 45000

            val buff = object : Buff() {
                override val name: String = "$TWO_SET_BUFF_NAME (Haste)"
                override val durationMs: Int = 10000

                override fun modifyStats(sp: SimParticipant): Stats {
                    return Stats(physicalHasteRating = 450.0)
                }
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(buff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val twoBuffAp = object : Buff() {
        override val name: String = "$TWO_SET_BUFF_NAME (AP vs Demons)"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats? {
            if(sp.sim.target.character.subTypes.contains(CharacterType.DEMON)) {
                return Stats(attackPower = 200)
            }

            return null
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuffHaste),
        Bonus(id, 2, twoBuffAp)
    )
}
