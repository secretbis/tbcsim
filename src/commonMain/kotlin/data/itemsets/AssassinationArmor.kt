package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class AssassinationArmor : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Assassination Armor (4 set)"

        fun fourSetEnergyReduction(): Double {
            return 10.0
        }
    }

    override val id: Int = 620

    val twoBuff = object : Buff() {
        override val name: String = "Assassination Armor (2 set)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.ROGUE_CAST_CHEAP_SHOT,
                Trigger.ROGUE_CAST_KIDNEY_SHOT
            )
            override val type: Type = Type.STATIC

            val hasteBuff = object : Buff() {
                override val name: String = "Assassination Armor (CS/KS haste)"
                override val durationMs: Int = 6000

                override fun modifyStats(sp: SimParticipant): Stats {
                    return Stats(physicalHasteRating = 160.0)
                }
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(hasteBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    // TODO: When Envenom and Eviscerate exist, they should check this buff to compute their costs
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
