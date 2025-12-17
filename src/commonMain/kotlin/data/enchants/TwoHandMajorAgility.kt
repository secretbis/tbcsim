package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class TwoHandMajorAgility(item: Item) : Enchant(item) {
    override val id: Int = 46461
    override val inventorySlot: Int = Constants.InventorySlot.TWO_HAND.ordinal
    override val name: String = "Major Agility (2H)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(agility = 35)
    }
}
