package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class CloakSpellPenetration(item: Item) : Enchant(item) {
    override val id: Int = 46509
    override val inventorySlot: Int = Constants.InventorySlot.BACK.ordinal
    override val name: String = "Spell Penetration"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellPen = 20)
    }
}
