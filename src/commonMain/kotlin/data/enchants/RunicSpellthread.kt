package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class RunicSpellthread(item: Item) : Enchant(item) {
    override val id: Int = 31372
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Runic Spellthread"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            spellDamage = 35,
            stamina = 20
        )
    }
}
