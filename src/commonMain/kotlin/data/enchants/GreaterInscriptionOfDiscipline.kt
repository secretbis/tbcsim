package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimIteration
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfDiscipline(item: Item) : Enchant(item) {
    override val id: Int = 35406
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of Discipline"

    override fun modifyStats(sim: SimIteration): Stats {
        return Stats(
            frostDamage = 18,
            shadowDamage = 18,
            arcaneDamage = 18,
            fireDamage = 18,
            natureDamage = 18,
            holyDamage = 18,
            spellCritRating = 10.0,
        )
    }
}
