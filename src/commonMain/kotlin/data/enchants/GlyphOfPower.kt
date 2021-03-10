package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlyphOfPower(item: Item) : Enchant(item) {
    override val id: Int = 35447
    override val inventorySlot: Int = Constants.InventorySlot.HEAD.ordinal
    override val name: String = "Glyph of Power"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            spellDamage = 22,
            spellHealing = 22,
            spellHitRating = 14.0,
        )
    }
}
