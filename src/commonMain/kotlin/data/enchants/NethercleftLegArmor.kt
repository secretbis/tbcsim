package data.enchants

import character.Stats
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class NethercleftLegArmor(item: Item) : Enchant(item) {
    override val id: Int = 35495
    override val inventorySlot: Int = Constants.InventorySlot.LEGS.ordinal
    override val name: String = "Nethercleft Leg Armor"

    override fun modifyStats(sp: SimParticipant): Stats {
        return Stats(
            stamina = 40,
            agility = 12
        )
    }
}
