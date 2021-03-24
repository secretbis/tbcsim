package data.itemsets

import character.*
import data.model.ItemSet

class GronnstalkersArmor : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Gronnstalker's Armor (2 set)"
        const val FOUR_SET_BUFF_NAME = "Gronnstalker's Armor (4 set)"

        fun twoSetAspectOfTheViperMIntMultiplier(): Double {
            return 1.05
        }

        fun fourSetSteadyShotDamageMultiplier(): Double {
            return 1.1
        }
    }
    override val id: Int = 669

    // TODO: Aspect of the Viper should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // TODO: Steady shot should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
