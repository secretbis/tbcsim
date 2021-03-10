package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class BracerSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 35425
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Spellpower (Bracer)"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(spellDamage = 15)
    }
}
