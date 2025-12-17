package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class RingStriking(item: Item) : Enchant(item) {
    override val id: Int = 46520
    override val inventorySlot: Int = Constants.InventorySlot.FINGER.ordinal
    override val name: String = "Striking"
    override val maxStacks = 2

    override fun modifyStats(sp: SimParticipant): Stats {
        val state = state(sp)
        var mod = 2.0 * state.currentStacks

        return Stats(whiteDamageFlatModifier = mod, yellowDamageFlatModifier = mod)
    }
}
