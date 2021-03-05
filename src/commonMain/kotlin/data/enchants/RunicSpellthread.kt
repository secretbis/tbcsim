package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class RunicSpellthread(item: Item) : Enchant(item) {
    override val id: Int = 31372
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Runic Spellthread"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 35,
            shadowDamage = 35,
            arcaneDamage = 35,
            fireDamage = 35,
            natureDamage = 35,
            holyDamage = 35,
            stamina = 20
        )
    }
}
