package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class Soulfrost(item: Item) : Enchant(item) {
    override val id: Int = 46538
    override val inventorySlot: Int = Constants.InventorySlot.WEAPON.ordinal
    override val name: String = "Soulfrost"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 50,
            shadowDamage = 50
        )
    }
}
