package `data`.items

import `data`.Constants
import `data`.buffs.Buffs
import `data`.itemsets.ItemSets
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
public class VengefulGladiatorsScaledGauntlets : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 33750

  public override var name: String = "Vengeful Gladiator's Scaled Gauntlets"

  public override var itemLevel: Int = 146

  public override var quality: Int = 4

  public override var icon: String = "inv_gauntlets_60.jpg"

  public override var inventorySlot: Int = 10

  public override var itemSet: ItemSet? = ItemSets.byId(583)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.PLATE

  public override var allowableClasses: Array<Constants.AllowableClass>? = arrayOf(
      Constants.AllowableClass.PALADIN
      )

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      strength = 40,
      stamina = 40,
      intellect = 22,
      physicalCritRating = 26.0
      )

  public override var sockets: Array<Socket> = arrayOf()

  public override var socketBonus: SocketBonus? = null

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(44300, "Improved Crusader Strike", this)
        )}

}