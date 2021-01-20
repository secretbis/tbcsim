package data.model

import character.Buff
import character.Proc
import character.Stats
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.model.deserialize.ItemDeserializer

@JsonDeserialize(using = ItemDeserializer::class)
open class Item : ModelBase() {
    // Item attributes
    var name: String = ""
    var itemLevel: Int = 0

    // TODO: This assumes physical damage types for items
    // TODO: Multiple damage types
    var minDmg: Double = 0.0
    var maxDmg: Double = 0.0
    var speed: Double = 1000.0

    // Stats
    var stats: Stats = Stats()

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
