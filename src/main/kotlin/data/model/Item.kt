package data.model

import character.Buff
import character.Proc
import character.Stats
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.Constants
import data.model.deserialize.ItemDeserializer

@JsonDeserialize(using = ItemDeserializer::class)
open class Item(

) : ModelBase() {
    // Item attributes
    var name: String = ""
    var itemLevel: Int = 0

    // TODO: Validate itemSubclass is indeed a subclass of the itemClass
    // TODO: The defaults here might cause some unexpected results if deserialization isn't careful
    var itemClass: Constants.ItemClass = Constants.ItemClass.WEAPON
    var itemSubclass: Constants.ItemSubclass = Constants.ItemSubclass.FIST

    // TODO: This assumes physical damage types for items
    // TODO: Multiple damage types
    var minDmg: Double = 0.0
    var maxDmg: Double = 0.0
    var speed: Double = 1000.0

    // Stats
    var stats: Stats = Stats()

    // Sockets
    var sockets: List<Socket> = listOf()
    var socketBonus: Pair<Constants.StatType, Int> = Pair(Constants.StatType.STAMINA, 0)
    val metaGemActive: Boolean
        get() {
            val byType = sockets.groupBy { it.color }
            return byType[Color.RED]?.size ?: 0 >= 2 &&
                    byType[Color.YELLOW]?.size ?: 0 >= 2 &&
                    byType[Color.BLUE]?.size ?: 0 >= 2
        }

    // Procs and effects
    var enchant: Buff? = null
    var temporaryEnhancement: Buff? = null
    var procs: List<Proc> = listOf()

    // Helpers
    val avgDmg: Double
        get() { return (minDmg + maxDmg) / 2}
    val dps: Double
        get() { return avgDmg / speed }
}
