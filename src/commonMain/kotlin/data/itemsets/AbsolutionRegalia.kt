package data.itemsets

import character.*
import data.model.ItemSet

class AbsolutionRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Absolution Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Absolution Regalia (4 set)"
    }
    override val id: Int = 674

    // https://tbc.wowhead.com/spell=38413/increased-shadow-word-pain-duration
    // Increases shadow word pain by 3 seconds
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // https://tbc.wowhead.com/spell=38412/improved-mind-blast
    // Increases mind blast damage by 10%
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
