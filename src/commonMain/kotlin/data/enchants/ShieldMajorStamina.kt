package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ShieldMajorStamina(item: Item) : Enchant(item) {
    override val id: Int = 34009
    override val inventorySlot: Int = Constants.InventorySlot.SHIELD.ordinal
    override val name: String = "Major Stamina"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            stamina = 18
        )
    }
}
