package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class RingStats(item: Item) : Enchant(item) {
    override val id: Int = 46519
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Stats (Ring)"
    override val maxStacks = 2

    override fun modifyStats(sp: SimParticipant): Stats {
        val state = state(sp)
        val stat = 4 * state.currentStacks

        return Stats(
            agility = stat,
            strength = stat,
            stamina = stat,
            spirit = stat,
            intellect = stat
        )
    }
}
