package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class BootsSurefooted(item: Item) : Enchant(item) {
    override val id: Int = 35418
    override val inventorySlot: Int = Constants.InventorySlot.FEET.ordinal
    override val name: String = "Surefooted"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            physicalCritRating = 10.0,
            physicalHitRating = 10.0
        )
    }
}
