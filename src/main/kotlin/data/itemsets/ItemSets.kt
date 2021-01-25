package data.itemsets

import data.model.ItemSet

object ItemSets {
    fun byId(id: Int): ItemSet? {
        return allItemSets.find { it.id == id }
    }

    // Every item in this list must have a distinct class name
    val allItemSets = listOf<ItemSet>(
        // TODO: Implement item sets
    )
}
