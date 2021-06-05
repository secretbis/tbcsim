package `data`.items

import `data`.Constants
import `data`.buffs.Buffs
import `data`.itemsets.ItemSets
import `data`.model.Color
import `data`.model.Item
import `data`.model.ItemSet
import `data`.model.Socket
import `data`.model.SocketBonus
import `data`.socketbonus.SocketBonuses
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
public class SkyshatterPants : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 31021

  public override var name: String = "Skyshatter Pants"

  public override var itemLevel: Int = 146

  public override var quality: Int = 4

  public override var icon: String = "inv_pants_mail_25.jpg"

  public override var inventorySlot: Int = 7

  public override var itemSet: ItemSet? = ItemSets.byId(682)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.MAIL

  public override var allowableClasses: Array<Constants.AllowableClass>? = arrayOf(
      Constants.AllowableClass.SHAMAN
      )

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      strength = 46,
      stamina = 67,
      intellect = 31,
      armor = 894,
      meleeCritRating = 39.0,
      rangedCritRating = 39.0,
      physicalHitRating = 21.0
      )

  public override var sockets: Array<Socket> = arrayOf(
      Socket(Color.BLUE)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(3015)

  public override var phase: Int = 3

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(21631, "Increased Mana Regen", this)
        )}

}
