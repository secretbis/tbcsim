package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class BracerSpellpower(item: Item) : Enchant(item) {
    override val id: Int = 35425
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Spellpower"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 15,
            shadowDamage = 15,
            arcaneDamage = 15,
            fireDamage = 15,
            natureDamage = 15,
            holyDamage = 15
        )
    }
}
