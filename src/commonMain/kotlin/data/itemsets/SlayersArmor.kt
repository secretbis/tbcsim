package data.itemsets

import character.*
import data.model.ItemSet

class SlayersArmor : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Slayer's Armor (2 set)"
        const val FOUR_SET_BUFF_NAME = "Slayer's Armor (4 set)"

        fun twoSetAdditionalSnDHaste(): Double {
            return 0.05
        }

        fun fourSetGeneratorDamageIncreasePercent(): Double {
            return 6.0
        }
    }
    override val id: Int = 668

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
