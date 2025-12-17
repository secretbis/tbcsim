package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class GlovesSpellStrike(item: Item) : Enchant(item) {
    override val id: Int = 46516
    override val inventorySlot: Int = Constants.InventorySlot.HANDS.ordinal
    override val name: String = "Spell Strike"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellHitRating = 15.0)
    }
}
