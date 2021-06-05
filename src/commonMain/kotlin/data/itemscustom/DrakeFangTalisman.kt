package data.itemscustom

import character.Stats
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import kotlin.js.JsExport

@JsExport
class DrakeFangTalisman : Item() {
    override var isAutoGenerated: Boolean = false
    override var id: Int = 19406
    override var name: String = "Drake Fang Talisman"
    override var itemLevel: Int = 75
    override var quality: Int = 4
    override var icon: String = "inv_misc_bone_06.jpg"
    override var inventorySlot: Int = 12
    override var itemSet: ItemSet? = null
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.MISC
    override var minDmg: Double = 0.0
    override var maxDmg: Double = 0.0
    override var speed: Double = 0.0
    override var stats: Stats = Stats(
        physicalHitRating = 20.0,
        rangedAttackPower = 56,
        attackPower = 56

    )
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null
    override var phase = 1
}
