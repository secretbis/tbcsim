package character

import data.model.Item

// This represents a buff that always needs a specific item as context
// e.g. weapon enchants
abstract class ItemBuff(open var sourceItems: List<Item>) : Buff()
