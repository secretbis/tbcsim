package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlovesMajorSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 35441
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Major Spellpower (Gloves)"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(spellDamage = 20)
    }
}
