package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BracerMajorIntellect(item: Item) : Enchant(item) {
    override val id: Int = 46496
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Major Intellect"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            intellect = 12
        )
    }
}
