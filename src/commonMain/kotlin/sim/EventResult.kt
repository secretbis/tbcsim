package sim

import kotlin.js.JsExport

@JsExport
enum class EventResult {
    RESIST,
    PARTIAL_RESIST_CRIT,
    PARTIAL_RESIST_HIT,
    MISS,
    DODGE,
    PARRY,
    GLANCE,
    BLOCK,
    BLOCKED_CRIT,
    CRIT,
    CRUSH,
    HIT,
    NONE
}
