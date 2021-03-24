package data.itemsets

import character.*
import data.model.ItemSet

class AbsolutionRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Absolution Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Absolution Regalia (4 set)"
    }
    override val id: Int = 674

    // TODO: Shadow Word Pain should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // TODO: Mind Blast should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
