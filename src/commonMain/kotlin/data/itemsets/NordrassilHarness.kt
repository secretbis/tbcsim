package data.itemsets

import character.*
import data.model.ItemSet

class NordrassilHarness : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Nordrassil Harness (2 set)"
        const val FOUR_SET_BUFF_NAME = "Nordrassil Harness (4 set)"
    }
    override val id: Int = 641

    // The two-set does not matter at all

    // TODO: Shred and Lacerate should check this buff once they exist
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
