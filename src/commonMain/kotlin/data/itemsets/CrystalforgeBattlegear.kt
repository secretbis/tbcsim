package data.itemsets

import character.Buff
import data.model.ItemSet

class CrystalforgeBattlegear : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Crystalforge Battlegear (2 set)"
        const val FOUR_SET_BUFF_NAME = "Crystalforge Battlegear (4 set)"
    }
    override val id: Int = 629

    // TODO: Judgements should check this buff once they exist
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // TODO: Judgements should check this buff once they exist, but it probably doesn't matter
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
