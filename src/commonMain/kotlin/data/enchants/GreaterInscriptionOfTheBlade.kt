package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfTheBlade(item: Item) : Enchant(item) {
    override val id: Int = 35439
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of the Blade"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            attackPower = 20,
            rangedAttackPower = 20,
            meleeCritRating = 15.0,
            rangedCritRating = 15.0
        )
    }
}
