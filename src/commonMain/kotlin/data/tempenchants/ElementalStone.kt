package data.tempenchants

import character.Stats
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.SimParticipant

class ElementalStone(item: Item) : TempEnchant(item) {
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Elemental Sharpening Stone"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            meleeCritRating = 28.0
        )
    }
}
