package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class BracerSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 46498
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Spellpower (Bracer)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellDamage = 15)
    }
}
