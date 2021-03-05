package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlovesSuperiorAgility(item: Item) : Enchant(item) {
    override val id: Int = 25080
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Superior Agility"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            agility = 15
        )
    }
}
