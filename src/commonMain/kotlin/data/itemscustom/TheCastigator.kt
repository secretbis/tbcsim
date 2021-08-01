package data.itemscustom

import character.*
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import kotlin.js.JsExport

@JsExport
class TheCastigator : Item() {
    override var isAutoGenerated: Boolean = false

    override var id: Int = 22808
    override var name: String = "The Castigator"
    override var itemLevel: Int = 83
    override var quality: Int = 4
    override var icon: String = "inv_mace_1h_stratholme_d_01.jpg"
    override var itemSet: ItemSet? = null
    override var inventorySlot: Int = 13
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.MACE_1H
    override var allowableClasses: Array<Constants.AllowableClass>? = null
    override var minDmg: Double = 119.0
    override var maxDmg: Double = 221.0
    override var speed: Double = 2600.0
    override var stats: Stats = Stats(
        stamina = 9,
        meleeCritRating = 14.0,
        rangedCritRating = 14.0,
        physicalHitRating = 10.0,
        attackPower = 16,
        rangedAttackPower = 16
    )
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null
    override var phase = 1
}
