package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ShieldIntellect(item: Item) : Enchant(item) {
    override val id: Int = 27945
    override val inventorySlot: Int = Constants.InventorySlot.SHIELD.ordinal
    override val name: String = "Intellect (Shield)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            intellect = 12
        )
    }
}
