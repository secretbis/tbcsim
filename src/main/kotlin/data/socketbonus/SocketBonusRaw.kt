package data.socketbonus

import data.Constants

abstract class SocketBonusRaw {
    abstract var id: Int
    abstract var stat: Constants.StatType
    abstract var amount: Int
    open var stat2: Constants.StatType? = null
    open var amount2: Int = 0
}
