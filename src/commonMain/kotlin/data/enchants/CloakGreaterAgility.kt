package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class CloakGreaterAgility(item: Item) : Enchant(item) {
    override val id: Int = 35432
    override val inventorySlot: Int = Constants.InventorySlot.BACK.ordinal
    override val name: String = "Greater Agility"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(agility = 12)
    }
}
