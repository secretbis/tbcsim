package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GlovesMajorStrength(item: Item) : Enchant(item) {
    override val id: Int = 46515
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Major Strength"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            strength = 15
        )
    }
}
