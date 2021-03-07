package data.model

import character.Stats
import kotlin.js.JsExport

@JsExport
class SocketBonus(
    val stats: Stats
) {
    fun modifyStats(): Stats {
        return this.stats
    }
}
