package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class Sunfire(item: Item) : Enchant(item) {
    override val id: Int = 46540
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Sunfire"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            fireDamage = 50,
            arcaneDamage = 50
        )
    }
}
