package character

import data.model.Item
import sim.Event
import sim.SimParticipant

abstract class ItemProc(val sourceItems: List<Item>) : Proc() {
    override val requiresItem: Boolean = true

    // For an ItemProc, always check that the proc attempt came from the sourceItem
    override fun shouldProc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?): Boolean {
        val validItem = sourceItems.any { items?.contains(it) == true }
        return validItem && super.shouldProc(sp, sourceItems, ability, event)
    }
}
