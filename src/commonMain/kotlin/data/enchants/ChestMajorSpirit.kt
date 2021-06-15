package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ChestMajorSpirit(item: Item) : Enchant(item) {
    override val id: Int = 33990
    override val inventorySlot: Int = Constants.InventorySlot.CHEST.ordinal
    override val name: String = "Major Spirit"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spirit = 15)
    }
}
