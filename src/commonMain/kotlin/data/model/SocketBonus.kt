package data.model

import character.Stats
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
class SocketBonus(
    val stats: Stats
) {
    fun modifyStats(): Stats {
        return this.stats
    }
}
