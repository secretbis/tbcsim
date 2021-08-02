package data.enchants

import character.Stats
import character.classes.hunter.Hunter
import data.Constants
import data.model.Enchant
import data.model.Item
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class BiznicksAccurascope(item: Item) : Enchant(item) {
    override val id: Int = 18283
    override val inventorySlot: Int = Constants.InventorySlot.RANGED.ordinal
    override val name: String = "Biznicks 247x128 Accurascope"

    override fun modifyStats(sp: SimParticipant): Stats {
        return if(sp.character.klass is Hunter) {
            Stats(
                physicalHitRating = 30.0
            )
        } else Stats()
    }
}
