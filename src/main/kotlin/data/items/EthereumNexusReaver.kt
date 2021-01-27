package `data`.items

import `data`.Constants
import `data`.model.Color
import `data`.model.Item
import `data`.model.ItemSet
import `data`.model.Socket
import `data`.model.SocketBonus
import `data`.socketbonus.SocketBonuses
import character.Buff
import character.Stats
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List

public class EthereumNexusReaver : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Ethereum Nexus-Reaver"

  public override var itemLevel: Int = 120

  public override var itemSet: ItemSet? = null

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.WEAPON

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.AXE_2H

  public override var minDmg: Double = 346.0

  public override var maxDmg: Double = 519.0

  public override var speed: Double = 3700.0

  public override var stats: Stats = Stats(
      strength = 50,
      physicalCritRating = 30.0
      )

  public override var sockets: List<Socket> = listOf(
      Socket(Color.RED),
      Socket(Color.YELLOW),
      Socket(Color.YELLOW)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(3094)

  public override var buffs: List<Buff> = listOf()
}
