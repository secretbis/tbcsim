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
public class SporelingsFirestick : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var id: Int = 29149

  public override var name: String = "Sporeling's Firestick"

  public override var itemLevel: Int = 91

  public override var quality: Int = 3

  public override var icon: String = "inv_staff_02.jpg"

  public override var inventorySlot: Int = 26

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.WAND

  public override var allowableClasses: Array<Constants.AllowableClass>? = null

  public override var minDmg: Double = 88.0

  public override var maxDmg: Double = 164.0

  public override var speed: Double = 1300.0

  public override var stats: Stats = Stats(
      stamina = 12,
      intellect = 9
      )

  public override var sockets: Array<Socket> = arrayOf()

  public override var socketBonus: SocketBonus? = null

  public override var phase: Int = 1

  public override val buffs: List<Buff> by lazy {
        listOfNotNull(
        Buffs.byIdOrName(9416, "Increase Spell Dam 11", this)
        )}

}
