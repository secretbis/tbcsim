package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GlovesBlasting(item: Item) : Enchant(item) {
    override val id: Int = 35439
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Blasting"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            spellCritRating = 10.0
        )
    }
}
