package data.tempenchants

import character.Stats
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.SimParticipant

class AdamantiteStone(item: Item) : TempEnchant(item) {
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Adamantite Stone"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            whiteDamageFlatModifier = 12.0,
            yellowDamageFlatModifier = 12.0,
            meleeCritRating = 14.0
        )
    }
}
