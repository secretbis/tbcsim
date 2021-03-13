package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class WeaponMajorSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 35456
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Major Spellpower (Weapon)"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(spellDamage = 40
        )
    }
}
