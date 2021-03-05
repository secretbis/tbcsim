package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class RingStriking(item: Item) : Enchant(item) {
    override val id: Int = 46520
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Striking"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            whiteDamageFlatModifier = 2.0,
            yellowDamageFlatModifier = 2.0
        )
    }
}
