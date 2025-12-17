package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import kotlin.js.JsExport
import sim.SimParticipant

@JsExport
class WeaponPotency(item: Item) : Enchant(item) {
    override val id: Int = 46537
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Potency"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(strength = 20)
    }
}
