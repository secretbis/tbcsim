package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class KhoriumScope(item: Item) : Enchant(item) {
    override val id: Int = 23808
    override val inventorySlot: Int = Constants.InventorySlot.RANGED.ordinal
    override val name: String = "Khorium Scope"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            whiteDamageFlatModifier = 12.0,
            yellowDamageFlatModifier = 12.0
        )
    }
}
