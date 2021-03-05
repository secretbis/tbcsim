package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class NethercobraLegArmor(item: Item) : Enchant(item) {
    override val id: Int = 35490
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Nethercobra Leg Armor"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            attackPower = 50,
            rangedAttackPower = 50,
            physicalCritRating = 12.0
        )
    }
}
