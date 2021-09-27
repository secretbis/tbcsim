package data.itemscustom

import character.Stats
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import kotlin.js.JsExport

@JsExport
class ChimaerahideLeggingsOfShadowWrath : Item() {
    override var isAutoGenerated: Boolean = false
    override var id: Int = 31212
    override var name: String = "Chimaerahide Leggings of Shadow Wrath"
    override var itemLevel: Int = 109
    override var quality: Int = 3
    override var icon: String = "inv_pants_cloth_06.jpg"
    override var inventorySlot: Int = 7
    override var itemSet: ItemSet? = null
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.CLOTH
    override var minDmg: Double = 0.0
    override var maxDmg: Double = 0.0
    override var speed: Double = 0.0
    override var stats: Stats = Stats(
        shadowDamage = 85,
    )
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null
    override var phase = 1
}
