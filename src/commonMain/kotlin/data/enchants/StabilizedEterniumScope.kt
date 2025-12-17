package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class StabilizedEterniumScope(item: Item) : Enchant(item) {
    override val id: Int = 30255
    override val inventorySlot: Int = Constants.InventorySlot.RANGED.ordinal
    override val name: String = "Stabilized Eternium Scope"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(gunCritRating = 28.0, bowCritRating = 28.0)
    }
}
