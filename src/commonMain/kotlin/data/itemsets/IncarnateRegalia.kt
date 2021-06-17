package data.itemsets

import character.Buff
import data.model.ItemSet

class IncarnateRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Incarnate Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Incarnate Regalia (4 set)"

        fun twoSetDurationIncreaseMs(): Int {
            return 3000
        }

        fun fourSetDmgMultiplierPct(): Double {
            return 0.05
        }
    }
    override val id: Int = 664

    // TODO: Shadowfiend should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // TODO: Mind Flay and Smite should check this buff once they exist
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
