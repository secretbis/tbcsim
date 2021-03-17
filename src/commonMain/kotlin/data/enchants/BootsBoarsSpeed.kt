package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BootsBoarsSpeed(item: Item) : Enchant(item) {
    override val id: Int = 46470
    override val inventorySlot: Int = Constants.InventorySlot.FEET.ordinal
    override val name: String = "Boar's Speed"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            stamina = 9
        )
    }
}
