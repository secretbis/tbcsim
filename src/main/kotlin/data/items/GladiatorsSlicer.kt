package `data`.items

import `data`.Constants
import `data`.buffs.Buffs
import `data`.model.Item
import `data`.model.ItemSet
import `data`.model.Socket
import `data`.model.SocketBonus
import character.Buff
import character.Stats
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class GladiatorsSlicer : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Gladiator's Slicer"

  public override var itemLevel: Int = 123

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.SWORD_1H

  public override var minDmg: Double = 189.0

  public override var maxDmg: Double = 285.0

  public override var speed: Double = 2600.0

  public override var stats: Stats = Stats(
      stamina = 21,
      physicalCritRating = 15.0,
      physicalHitRating = 9.0
      )

  public override var sockets: List<Socket> = listOf()

  public override var socketBonus: SocketBonus? = null

  public override var buffs: List<Buff> = listOfNotNull(
      Buffs.byIdOrName(9335, "Attack Power 28", this)
      )
}
