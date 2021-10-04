package data.itemscustom

import character.Stats
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import kotlin.js.JsExport

@JsExport
class ElementalistBraceletsOfShadowWrath : Item() {
    override var isAutoGenerated: Boolean = false
    override var id: Int = 24692
    override var name: String = "Elementalist Bracelets of Shadow Wrath"
    override var itemLevel: Int = 120
    override var quality: Int = 2
    override var icon: String = "inv_bracer_10.jpg"
    override var inventorySlot: Int = 9
    override var itemSet: ItemSet? = null
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.CLOTH
    override var minDmg: Double = 0.0
    override var maxDmg: Double = 0.0
    override var speed: Double = 0.0
    override var stats: Stats = Stats(
        shadowDamage = 45,
    )
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null
    override var phase = 1
}
