package character

import data.Constants
import data.model.Item
import kotlin.js.JsExport

// This represents a buff that always needs a specific item as context
// e.g. weapon enchants
@JsExport
abstract class ItemBuff(open var sourceItems: List<Item>) : Buff() {
    override val icon: String = if(sourceItems.isNotEmpty()) { sourceItems[0].icon } else { Constants.UNKNOWN_ICON }
}
