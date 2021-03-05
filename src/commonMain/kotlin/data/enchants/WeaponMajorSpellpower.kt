package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class WeaponMajorSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 35456
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Major Spellpower"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 40,
            shadowDamage = 40,
            arcaneDamage = 40,
            fireDamage = 40,
            natureDamage = 40,
            holyDamage = 40
        )
    }
}
