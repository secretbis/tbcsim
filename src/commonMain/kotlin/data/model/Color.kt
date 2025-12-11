package data.model

import data.Constants
import kotlin.js.JsExport

@JsExport
enum class Color(val mask: Int, val itemSubclass: Constants.ItemSubclass) {
    META(1, Constants.ItemSubclass.META),

    RED(2, Constants.ItemSubclass.RED),
    YELLOW(4, Constants.ItemSubclass.YELLOW),
    BLUE(8, Constants.ItemSubclass.BLUE),

    ORANGE(6, Constants.ItemSubclass.ORANGE),
    PURPLE(10, Constants.ItemSubclass.PURPLE),
    GREEN(12, Constants.ItemSubclass.GREEN);

    companion object {
        fun matchesColor(gem: Gem, color: Color): Boolean {
            return gem.color.mask and color.mask != 0
        }
    }
}
