package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlyphOfTheOutcast(item: Item) : Enchant(item) {
    override val id: Int = 37891
    override val inventorySlot: Int = Constants.InventorySlot.HEAD.ordinal
    override val name: String = "Glyph of the Outcast"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            strength = 17,
            intellect = 16
        )
    }
}
