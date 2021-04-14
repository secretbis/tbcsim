package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GreaterAgility(item: Item) : Enchant(item) {
    override val id: Int = 46461
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Greater Agility (1H)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            agility = 20
        )
    }
}
