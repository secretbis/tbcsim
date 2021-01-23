package character

import data.model.Item
import sim.SimIteration

abstract class ItemProc(val sourceItems: List<Item>) : Proc() {
    override val requiresItem: Boolean = true

    // For an ItemProc, always check that the proc attempt came from the sourceItem
    override fun shouldProc(sim: SimIteration, items: List<Item>?, ability: Ability?): Boolean {
        val validItem = sourceItems.any { items?.contains(it) == true }
        return validItem && super.shouldProc(sim, sourceItems, ability)
    }
}
