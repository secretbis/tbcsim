package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfTheBlade(item: Item) : Enchant(item) {
    override val id: Int = 35439
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of the Blade"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            attackPower = 20,
            rangedAttackPower = 20,
            physicalCritRating = 15.0
        )
    }
}
