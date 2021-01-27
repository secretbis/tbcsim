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

public class HardenedStoneShard : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Hardened Stone Shard"

  public override var itemLevel: Int = 91

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.DAGGER

  public override var minDmg: Double = 79.0

  public override var maxDmg: Double = 120.0

  public override var speed: Double = 1800.0

  public override var stats: Stats = Stats(
      stamina = 16,
      physicalHitRating = 12.0
      )

  public override var sockets: List<Socket> = listOf()

  public override var socketBonus: SocketBonus? = null

  public override var buffs: List<Buff> = listOfNotNull(
      Buffs.byIdOrName(9332, "Attack Power 22", this)
      )
}
