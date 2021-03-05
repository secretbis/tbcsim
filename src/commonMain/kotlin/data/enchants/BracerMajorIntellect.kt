package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class BracerMajorIntellect(item: Item) : Enchant(item) {
    override val id: Int = 35423
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Major Intellect"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            intellect = 12
        )
    }
}
