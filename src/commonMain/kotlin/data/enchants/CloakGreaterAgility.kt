package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class CloakGreaterAgility(item: Item) : Enchant(item) {
    override val id: Int = 46505
    override val inventorySlot: Int = Constants.InventorySlot.BACK.ordinal
    override val name: String = "Greater Agility"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(agility = 12)
    }
}
