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

public class MercilessGladiatorsDecapitator : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Merciless Gladiator's Decapitator"

  public override var itemLevel: Int = 136

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.AXE_2H

  public override var minDmg: Double = 365.0

  public override var maxDmg: Double = 549.0

  public override var speed: Double = 3600.0

  public override var stats: Stats = Stats(
      stamina = 55,
      physicalCritRating = 42.0,
      physicalHitRating = 18.0
      )

  public override var sockets: List<Socket> = listOf()

  public override var socketBonus: SocketBonus? = null

  public override var buffs: List<Buff> = listOfNotNull(
      Buffs.byIdOrName(15828, "Attack Power 84", this)
      )
}
