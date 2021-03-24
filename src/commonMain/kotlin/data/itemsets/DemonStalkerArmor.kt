package data.itemsets

import character.Buff
import data.model.ItemSet

class DemonStalkerArmor : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Demon Stalker Armor (4 set)"

        fun fourSetMultiShotCostReductionPct(): Int {
            return 10
        }
    }

    override val id: Int = 651

    // The two-set is not relevant

    // TODO: Multi-Shot should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
