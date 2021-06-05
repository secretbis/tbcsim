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
public class VengefulGladiatorsDragonhideTunic : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 33675

  public override var name: String = "Vengeful Gladiator's Dragonhide Tunic"

  public override var itemLevel: Int = 146

  public override var quality: Int = 4

  public override var icon: String = "inv_chest_leather_15.jpg"

  public override var inventorySlot: Int = 5

  public override var itemSet: ItemSet? = ItemSets.byId(584)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.LEATHER

  public override var allowableClasses: Array<Constants.AllowableClass>? = arrayOf(
      Constants.AllowableClass.DRUID
      )

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      strength = 30,
      agility = 31,
      stamina = 54,
      intellect = 22,
      armor = 529,
      resilienceRating = 26.0
      )

  public override var sockets: Array<Socket> = arrayOf(
      Socket(Color.RED),
      Socket(Color.RED),
      Socket(Color.YELLOW)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(2874)

  public override var phase: Int = 3

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(39927, "Armor Penetration 84", this)
        )}

}
