package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BracerBrawn(item: Item) : Enchant(item) {
    override val id: Int = 35420
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Brawn"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            strength = 12
        )
    }
}
