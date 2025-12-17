package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class GlyphOfFerocity(item: Item) : Enchant(item) {
    override val id: Int = 35452
    override val inventorySlot: Int = Constants.InventorySlot.HEAD.ordinal
    override val name: String = "Glyph of Ferocity"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(attackPower = 24, rangedAttackPower = 24, physicalHitRating = 16.0)
    }
}
