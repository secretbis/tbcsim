package data.itemsets

import character.Buff
import data.model.ItemSet

class RiftStalkerArmor : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Rift Stalker Armor (4 set)"

        fun fourSetSteadyShotBonusCritPct(): Double {
            return 0.05
        }
    }

    override val id: Int = 652

    // The two-set is not relevant

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_pants_mail_15.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
