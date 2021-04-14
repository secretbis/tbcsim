package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class MightOfTheScourge(item: Item) : Enchant(item) {
    override val id: Int = 29483
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Might of the Scourge"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            attackPower = 26,
            rangedAttackPower = 26,
            meleeCritRating = 14.0,
            rangedCritRating = 14.0
        )
    }
}
