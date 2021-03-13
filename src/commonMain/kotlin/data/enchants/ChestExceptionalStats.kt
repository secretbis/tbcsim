package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class ChestExceptionalStats(item: Item) : Enchant(item) {
    override val id: Int = 35429
    override val inventorySlot: Int = Constants.InventorySlot.CHEST.ordinal
    override val name: String = "Exceptional Stats"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            agility = 6,
            strength = 6,
            stamina = 6,
            spirit = 6,
            intellect = 6
        )
    }
}
