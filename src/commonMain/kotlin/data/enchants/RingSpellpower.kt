package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class RingSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 46518
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Spellpower (Ring)"
    override val maxStacks = 2

    override fun modifyStats(sp: SimParticipant): Stats {
        val state = state(sp)
        return Stats(spellDamage = 12 * state.currentStacks)
    }
}
