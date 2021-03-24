package data.itemsets

import character.Buff
import data.model.ItemSet

class CataclysmHarness : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Cataclysm Harness (4 set)"

        fun fourSetAdditionalFlurryHaste(): Double {
            return 0.05
        }
    }

    override val id: Int = 636

    // The two-set is entirely useless

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
