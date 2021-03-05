package data.model

import character.ItemBuff

abstract class TempEnchant(val item: Item) : ItemBuff(listOf(item)) {
    abstract val inventorySlot: Int

    // While these are technically time limited, for sim purposes it will never matter
    override val durationMs: Int = -1
}
