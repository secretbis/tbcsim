package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GlovesThreat(item: Item) : Enchant(item) {
    override val id: Int = 25072
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Threat"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            innateThreatMultiplier = 1.02
        )
    }
}
