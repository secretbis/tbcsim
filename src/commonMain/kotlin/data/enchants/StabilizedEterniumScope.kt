package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class StabilizedEterniumScope(item: Item) : Enchant(item) {
    override val id: Int = 30255
    override val inventorySlot: Int = Constants.InventorySlot.RANGED.ordinal
    override val name: String = "Stabilized Eternium Scope"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            gunCritRating = 28.0,
            bowCritRating = 28.0
        )
    }
}
