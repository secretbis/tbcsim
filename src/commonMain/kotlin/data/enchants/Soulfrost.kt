package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class Soulfrost(item: Item) : Enchant(item) {
    override val id: Int = 46538
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Soulfrost"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(frostDamage = 54, shadowDamage = 54)
    }
}
