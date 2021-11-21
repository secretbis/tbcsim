package data.tempenchants

import character.Stats
import data.Constants
import data.model.Item
import data.model.TempEnchant
import sim.SimParticipant

class BuggedHunterAdamantiteStone(item: Item) : TempEnchant(item) {
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Bugged? Hunter Adamantite Stone"
    override val icon: String = "inv_stone_sharpeningstone_07.jpg"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            whiteDamageFlatModifier = 12.0,
            yellowDamageFlatModifier = 12.0,
            meleeCritRating = 14.0,
            rangedCritRating = 14.0
        )
    }
}
