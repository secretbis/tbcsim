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
public class SunwardCrest : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 34675

  public override var name: String = "Sunward Crest"

  public override var itemLevel: Int = 115

  public override var quality: Int = 4

  public override var icon: String = "inv_shield_48.jpg"

  public override var inventorySlot: Int = 14

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.SHIELD

  public override var allowableClasses: Array<Constants.AllowableClass>? = null

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      stamina = 31,
      armor = 4872,
      spellHitRating = 13.0,
      blockRating = 18.0
      )

  public override var sockets: Array<Socket> = arrayOf()

  public override var socketBonus: SocketBonus? = null

  public override var phase: Int = 5

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(18049, "Increase Spell Dam 26", this),
        Buffs.byIdOrName(-1, "Block Value 122", this)
        )}

}
