package data.itemscustom

import character.Buff
import character.CharacterType
import character.Stats
import data.Constants
import data.buffs.Buffs
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class MindQuickeningGem : Item() {
    override var isAutoGenerated: Boolean = false
    override var id: Int = 19339
    override var name: String = "Mind Quickening Gem"
    override var itemLevel: Int = 76
    override var quality: Int = 4
    override var icon: String = "spell_nature_wispheal.jpg"
    override var inventorySlot: Int = 12
    override var itemSet: ItemSet? = null
    override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR
    override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.MISC
    override var minDmg: Double = 0.0
    override var maxDmg: Double = 0.0
    override var speed: Double = 0.0
    override var stats: Stats = Stats()
    override var sockets: Array<Socket> = arrayOf()
    override var socketBonus: SocketBonus? = null
    override var phase = 1

    override val buffs: List<Buff> by lazy {
        listOfNotNull(
            Buffs.byIdOrName(23723, "Mind Quickening", this)
        )
    }

}
