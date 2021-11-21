package data.itemsets

import character.Buff
import data.model.ItemSet

class OblivionRaiment : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Oblivion Raiment (2 set)"
        const val FOUR_SET_BUFF_NAME = "Oblivion Raiment (4 set)"
    }
    override val id: Int = 644

    // TODO: Warlock pets should check this buff when those exist
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_chest_cloth_29.jpg"
        override val durationMs: Int = -1
    }

    // TODO: Seed of Corruption should check this buff when it exists
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_chest_cloth_29.jpg"
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
