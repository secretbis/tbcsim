package data.itemscustom

import character.Stats
import data.Constants
import data.model.Item
import data.model.ItemSet
import data.model.Socket
import data.model.SocketBonus
import kotlin.js.JsExport

@JsExport
public class FlawlessWandOfShadowWrath : Item() {
  public override var isAutoGenerated: Boolean = false
  public override var id: Int = 25295
  public override var name: String = "Flawless Wand of Shadow Wrath"
  public override var itemLevel: Int = 120
  public override var quality: Int = 2
  public override var icon: String = "inv_wand_16.jpg"
  public override var inventorySlot: Int = 26
  public override var itemSet: ItemSet? = null
  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON
  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.WAND
  public override var allowableClasses: Array<Constants.AllowableClass>? = null
  public override var minDmg: Double = 137.0
  public override var maxDmg: Double = 255.0
  public override var speed: Double = 1700.0
  public override var stats: Stats = Stats(
      shadowDamage = 25,
      )
  public override var sockets: Array<Socket> = arrayOf()
  public override var socketBonus: SocketBonus? = null
  public override var phase: Int = 1
}
