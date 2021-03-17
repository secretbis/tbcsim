package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GlovesMajorSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 46514
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Major Spellpower (Gloves)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellDamage = 20)
    }
}
