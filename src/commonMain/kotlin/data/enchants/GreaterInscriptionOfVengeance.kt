package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfVengeance(item: Item) : Enchant(item) {
    override val id: Int = 35417
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of Vengeance"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            attackPower = 30,
            rangedAttackPower = 30,
            physicalCritRating = 10.0
        )
    }
}
