package data.itemsets

import character.Buff
import data.model.ItemSet

class CycloneHarness : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Cyclone Harness (2 set)"
        const val FOUR_SET_BUFF_NAME = "Cyclone Harness (4 set)"

        fun twoSetStrengthOfEarthBonus(): Int {
            return 12
        }

        fun fourSetStormstrikeBonus(): Int {
            return 30
        }
    }

    override val id: Int = 633

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_pants_mail_15.jpg"
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_pants_mail_15.jpg"
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
