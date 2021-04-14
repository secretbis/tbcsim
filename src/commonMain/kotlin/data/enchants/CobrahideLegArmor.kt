package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class CobrahideLegArmor(item: Item) : Enchant(item) {
    override val id: Int = 35488
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Cobrahide Leg Armor"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            attackPower = 40,
            rangedAttackPower = 40,
            meleeCritRating = 10.0,
            rangedCritRating = 10.0
        )
    }
}
