package data.itemsets

import character.Buff
import data.model.ItemSet

class AldorRegalia : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Aldor Regalia (4 set)"
    }
    override val id: Int = 648

    // TODO: The 2-set may or may not be relevant someday, but it isn't relevant right now

    // TODO: PoM, Blast Wave, and Ice Block should check this buff once they exist
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_crown_01.jpg"
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
