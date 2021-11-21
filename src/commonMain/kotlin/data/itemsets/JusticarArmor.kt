package data.itemsets

import character.Buff
import data.model.ItemSet

class JusticarArmor : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Justicar Armor (2 set)"
        const val FOUR_SET_BUFF_NAME = "Justicar Armor (4 set)"
    }
    override val id: Int = 625

    // TODO: SoR, SoV, SoB, SoM should check this buff once it exists
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_helmet_15.jpg"
    }

    // TODO: Holy Shield should check this buff once it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val icon: String = "inv_helmet_15.jpg"
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
