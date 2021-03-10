package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class RingSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 46518
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Spellpower (Ring)"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(spellDamage = 12)
    }
}
