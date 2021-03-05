package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class MightOfTheScourge(item: Item) : Enchant(item) {
    override val id: Int = 29483
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Might of the Scourge"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            attackPower = 26,
            rangedAttackPower = 26,
            physicalCritRating = 14.0
        )
    }
}
