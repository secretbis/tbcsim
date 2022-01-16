package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class GreaterInscriptionOfTheKnight(item: Item) : Enchant(item) {
    override val id: Int = 28911
    override val inventorySlot: Int = Constants.InventorySlot.SHOULDER.ordinal
    override val name: String = "Greater Inscription of the Knight"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            defenseRating = 15.0,
            dodgeRating = 10.0
        )
    }
}
