package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class PresenceOfMight(item: Item) : Enchant(item) {
    override val id: Int = 19782
    override val inventorySlot: Int = Constants.InventorySlot.HEAD.ordinal
    override val name: String = "Presence of Might"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            stamina = 10,
            defenseRating = 10.0,
            blockValue = 15.0
        )
    }
}
