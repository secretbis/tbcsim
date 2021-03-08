package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class RingStats(item: Item) : Enchant(item) {
    override val id: Int = 46519
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Stats (Ring)"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            agility = 4,
            strength = 4,
            stamina = 4,
            spirit = 4,
            intellect = 4
        )
    }
}
