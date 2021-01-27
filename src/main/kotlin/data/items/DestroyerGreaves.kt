package `data`.items

import `data`.Constants
import `data`.itemsets.ItemSets
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

public class DestroyerGreaves : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Destroyer Greaves"

  public override var itemLevel: Int = 133

  public override var itemSet: ItemSet? = ItemSets.byId(657)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.PLATE

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      strength = 52,
      stamina = 57,
      physicalCritRating = 32.0,
      physicalHitRating = 22.0
      )

  public override var sockets: List<Socket> = listOf(
      Socket(Color.RED)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(2885)

  public override var buffs: List<Buff> = listOf()
}
