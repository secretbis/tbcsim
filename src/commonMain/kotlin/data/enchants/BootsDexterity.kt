package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BootsDexterity(item: Item) : Enchant(item) {
    override val id: Int = 35400
    override val inventorySlot: Int = Constants.InventorySlot.FEET.ordinal
    override val name: String = "Dexterity"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            agility = 12
        )
    }
}
