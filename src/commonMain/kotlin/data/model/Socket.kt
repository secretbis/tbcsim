package data.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
class Socket(val color: Color) {
    var gem: Gem? = null

    // Only meta gems can go in meta sockets, and only meta sockets can hold meta gems
    fun canSocket(gem: Gem): Boolean {
        return if(color == Color.META) {
            gem.color == Color.META
        } else true
    }

    fun matches(): Boolean {
        return gem != null && Color.matchesColor(gem!!, color)
    }
}
