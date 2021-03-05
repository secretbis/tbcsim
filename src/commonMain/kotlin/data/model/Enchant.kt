package data.model

import character.ItemBuff
import kotlin.js.JsExport

@JsExport
abstract class Enchant(val item: Item) : ItemBuff(listOf(item)) {
    abstract val inventorySlot: Int
    // All of these are permanent
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    open var displayName: String? = null
        get() = field ?: name
}
