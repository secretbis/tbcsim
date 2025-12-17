package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class TwoHandSavagery(item: Item) : Enchant(item) {
    override val id: Int = 46462
    override val inventorySlot: Int = Constants.InventorySlot.TWO_HAND.ordinal
    override val name: String = "Savagery"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(attackPower = 75, rangedAttackPower = 75)
    }
}
