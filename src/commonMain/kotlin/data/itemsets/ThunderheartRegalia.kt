package data.itemsets

import character.*
import data.model.ItemSet

class ThunderheartRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Thunderheart Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Thunderheart Regalia (4 set)"
    }
    override val id: Int = 677

    // TODO: Mangle should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_helmet_94.jpg"
        override val durationMs: Int = -1
    }

    // TODO: Rip, Swipe, and Ferocious Bite should check this buff once they exist
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_helmet_94.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
