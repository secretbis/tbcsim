package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfDiscipline(item: Item) : Enchant(item) {
    override val id: Int = 35406
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of Discipline"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            spellDamage = 18,
            spellCritRating = 10.0,
        )
    }
}
