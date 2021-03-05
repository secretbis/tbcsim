package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GlovesSpellStrike(item: Item) : Enchant(item) {
    override val id: Int = 33994
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Spell Strike"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            spellHitRating = 15.0
        )
    }
}
