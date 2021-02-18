package util

import kotlin.js.JsExport

@JsExport
object Utils {
    fun <T> listWrap(arr: Array<T>): MutableList<T> {
        return arr.toMutableList()
    }
}
