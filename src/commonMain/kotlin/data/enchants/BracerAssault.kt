package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BracerAssault(item: Item) : Enchant(item) {
    override val id: Int = 34002
    override val inventorySlot: Int = Constants.InventorySlot.WRISTS.ordinal
    override val name: String = "Assault"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            attackPower = 24,
            rangedAttackPower = 24
        )
    }
}
