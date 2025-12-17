package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class GlovesSuperiorAgility(item: Item) : Enchant(item) {
    override val id: Int = 25080
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Superior Agility"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(agility = 15)
    }
}
