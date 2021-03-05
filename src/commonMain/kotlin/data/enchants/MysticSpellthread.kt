package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class MysticSpellthread(item: Item) : Enchant(item) {
    override val id: Int = 31371
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Mystic Spellthread"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 25,
            shadowDamage = 25,
            arcaneDamage = 25,
            fireDamage = 25,
            natureDamage = 25,
            holyDamage = 25,
            stamina = 15
        )
    }
}
