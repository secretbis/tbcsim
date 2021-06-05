package `data`.items

import `data`.Constants
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
public class ThunderheartLeggings : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 31044

  public override var name: String = "Thunderheart Leggings"

  public override var itemLevel: Int = 146

  public override var quality: Int = 4

  public override var icon: String = "inv_pants_leather_26.jpg"

  public override var inventorySlot: Int = 7

  public override var itemSet: ItemSet? = ItemSets.byId(676)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.LEATHER

  public override var allowableClasses: Array<Constants.AllowableClass>? = arrayOf(
      Constants.AllowableClass.DRUID
      )

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      strength = 53,
      agility = 41,
      stamina = 49,
      intellect = 12,
      armor = 737,
      physicalHitRating = 27.0
      )

  public override var sockets: Array<Socket> = arrayOf(
      Socket(Color.BLUE)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(3015)

  public override var phase: Int = 3

  public override val buffs: List<Buff> by lazy {
        listOf()}

}
