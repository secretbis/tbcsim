package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlyphOfFerocity(item: Item) : Enchant(item) {
    override val id: Int = 35452
    override val inventorySlot: Int = Constants.InventorySlot.HEAD.ordinal
    override val name: String = "Glyph of Ferocity"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            attackPower = 24,
            rangedAttackPower = 24,
            physicalHitRating = 16.0
        )
    }
}
