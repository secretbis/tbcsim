package data.itemsets

import character.*
import data.model.ItemSet

class OnslaughtBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Onslaught Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Onslaught Battlegear (4 set)"

        fun twoSetExecuteCostReduction(): Double {
            return 3.0
        }

        fun fourSetMSBTDamageMultiplier(): Double {
            return 1.05
        }
    }
    override val id: Int = 672

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
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
