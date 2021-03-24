package data.itemsets

import character.*
import data.model.ItemSet
import sim.SimParticipant

class SkyshatterRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Skyshatter Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Skyshatter Regalia (4 set)"

        fun fourSetLBDamageMultiplier(): Double {
            return 1.05
        }
    }
    override val id: Int = 684

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            // This is technically cheating, but practically speaking this will be 100% true
            return Stats(
                manaPer5Seconds = 15,
                spellCritRating = 35.0,
                spellDamage = 45
            )
        }
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
