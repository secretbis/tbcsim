package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BootsSurefooted(item: Item) : Enchant(item) {
    override val id: Int = 46491
    override val inventorySlot: Int = Constants.InventorySlot.FEET.ordinal
    override val name: String = "Surefooted"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            meleeCritRating = 10.0,
            rangedCritRating = 10.0,
            physicalHitRating = 10.0
        )
    }
}
