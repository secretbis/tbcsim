package data.model

import kotlin.js.JsExport

@JsExport
enum class Quality {
    JUNK,
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    META
}
