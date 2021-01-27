package `data`.items

import `data`.Constants
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

public class Fireguard : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Fireguard"

  public override var itemLevel: Int = 107

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.SWORD_1H

  public override var minDmg: Double = 94.0

  public override var maxDmg: Double = 176.0

  public override var speed: Double = 1600.0

  public override var stats: Stats = Stats(
      agility = 16,
      stamina = 23,
      physicalHitRating = 16.0
      )

  public override var sockets: List<Socket> = listOf()

  public override var socketBonus: SocketBonus? = null

  public override var buffs: List<Buff> = listOf()
}
