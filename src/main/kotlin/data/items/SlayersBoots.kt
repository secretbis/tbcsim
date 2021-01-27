package `data`.items

import `data`.Constants
import `data`.buffs.Buffs
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

public class SlayersBoots : Item() {
  public override var isAutoGenerated: Boolean = true

  public override var name: String = "Slayer's Boots"

  public override var itemLevel: Int = 154

  public override var itemSet: ItemSet? = ItemSets.byId(668)

  public override var itemClass: Constants.ItemClass? = Constants.ItemClass.ARMOR

  public override var itemSubclass: Constants.ItemSubclass? = Constants.ItemSubclass.LEATHER

  public override var minDmg: Double = 0.0

  public override var maxDmg: Double = 0.0

  public override var speed: Double = 0.0

  public override var stats: Stats = Stats(
      agility = 30,
      stamina = 24,
      physicalCritRating = 16.0,
      physicalHitRating = 28.0,
      expertiseRating = 24.0
      )

  public override var sockets: List<Socket> = listOf(
      Socket(Color.RED)
      )

  public override var socketBonus: SocketBonus? = SocketBonuses.byId(2941)

  public override var buffs: List<Buff> = listOfNotNull(
      Buffs.byIdOrName(15829, "Attack Power 86", this)
      )
}
