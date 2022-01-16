package data.itemsets

import character.*
import data.model.ItemSet

class OnslaughtArmor : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Onslaught Armor (4 set)"

        fun fourSetShieldSlamMultiplier(): Double {
            return 1.1
        }
    }
    override val id: Int = 673

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_helmet_98.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
