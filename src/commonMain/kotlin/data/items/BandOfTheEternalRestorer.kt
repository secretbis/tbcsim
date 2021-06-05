package `data`.items

import `data`.Constants
import `data`.buffs.Buffs
import `data`.model.Item
import `data`.model.ItemSet
import `data`.model.Socket
import `data`.model.SocketBonus
import character.Buff
import character.Stats
import kotlin.Array
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.js.JsExport

@JsExport
public class BandOfTheEternalRestorer : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 29309

  public override var name: String = "Band of the Eternal Restorer"

  public override var itemLevel: Int = 152

  public override var quality: Int = 4

  public override var icon: String = "inv_jewelry_ring_55.jpg"

  public override var inventorySlot: Int = 11

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.MISC

  public override var allowableClasses: Array<Constants.AllowableClass>? = null

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      stamina = 28,
      intellect = 25
      )

  public override var sockets: Array<Socket> = arrayOf()

  public override var socketBonus: SocketBonus? = null

  public override var phase: Int = 3

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(18040, "Increase Healing 64", this),
        Buffs.byIdOrName(21632, "Increased Mana Regen", this),
        Buffs.byIdOrName(35086, "Band of the Eternal Restorer", this)
        )}

}
