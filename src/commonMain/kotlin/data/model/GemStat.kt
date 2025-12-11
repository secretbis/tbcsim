package data.model

import data.Constants
import kotlin.js.JsExport

@JsExport
data class GemStat(
    val stat: Constants.StatType,
    val uncommonValue: Int = 0,
    val rareValue: Int = 0,
    val epicValue: Int = 0,
    val metaValue: Int = 0
)
