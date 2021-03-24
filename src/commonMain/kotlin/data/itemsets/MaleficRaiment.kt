package data.itemsets

import character.Buff
import data.model.ItemSet

class MaleficRaiment : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Malefic Raiment (4 set)"

        fun fourSetSBIncinerateDamageMultiplier(): Double {
            return 1.06
        }
    }

    override val id: Int = 670

    // Two set is not relevant

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
