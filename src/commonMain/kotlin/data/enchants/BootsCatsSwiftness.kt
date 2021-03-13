package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BootsCatsSwiftness(item: Item) : Enchant(item) {
    override val id: Int = 34007
    override val inventorySlot: Int = Constants.InventorySlot.FEET.ordinal
    override val name: String = "Cat's Swiftness"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            agility = 6
        )
    }
}
