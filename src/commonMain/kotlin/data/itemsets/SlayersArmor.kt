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

        fun fourSetGeneratorDamageMultiplier(): Double {
            return 1.06
        }
    }
    override val id: Int = 668

    // TODO: Slice and Dice should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // TODO: Backstab, Sinister Strike, Mutilate, and Hemorrhage should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
