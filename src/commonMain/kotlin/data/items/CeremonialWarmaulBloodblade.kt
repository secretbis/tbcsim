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
public class CeremonialWarmaulBloodblade : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 25763

  public override var name: String = "Ceremonial Warmaul Blood-blade"

  public override var itemLevel: Int = 103

  public override var quality: Int = 3

  public override var icon: String = "inv_weapon_shortblade_05.jpg"

  public override var inventorySlot: Int = 13

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.DAGGER

  public override var allowableClasses: Array<Constants.AllowableClass>? = null

  public override var minDmg: Double = 74.0

  public override var maxDmg: Double = 139.0

  public override var speed: Double = 1700.0

  public override var stats: Stats = Stats(
      agility = 13,
      stamina = 19
      )

  public override var sockets: Array<Socket> = arrayOf()

  public override var socketBonus: SocketBonus? = null

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(9334, "Attack Power 26", this)
        )}

}