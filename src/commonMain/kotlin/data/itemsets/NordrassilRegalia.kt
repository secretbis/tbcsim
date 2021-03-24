package data.itemsets

import character.*
import data.model.ItemSet

class NordrassilRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Nordrassil Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Nordrassil Regalia (4 set)"
    }
    override val id: Int = 643

    // The two-set does not matter at all

    // TODO: Starfire should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
