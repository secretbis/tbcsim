package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class GlovesBlasting(item: Item) : Enchant(item) {
    override val id: Int = 46512
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Blasting"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellCritRating = 10.0)
    }
}
