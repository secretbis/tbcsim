package data.model

import data.Constants

data class GemStat(
    val stat: Constants.StatType,
    val uncommonValue: Int = 0,
    val rareValue: Int = 0,
    val epicValue: Int = 0,
    val metaValue: Int = 0
)
