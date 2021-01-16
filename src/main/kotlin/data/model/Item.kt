package data.model

import character.Proc
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import character.Stats
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.model.deserialize.ItemProcsDeserializer
import data.model.deserialize.ItemStatsDeserializer

@JsonIgnoreProperties(ignoreUnknown = true)
open class Item (
    @JsonProperty("entry")
    override var id: Int,

    // Weapon attributes
    // TODO: Multiple damage types
    @JsonProperty("dmg_min1")
    var minDmg: Int = 0,
    @JsonProperty("dmg_max1")
    var maxDmg: Int = 0,
    @JsonProperty("delay")
    var speed: Int = 0,

    // Stats
    @JsonDeserialize(using = ItemStatsDeserializer::class)
    var stats: Stats = Stats(),

    // Procs and spells
    @JsonDeserialize(using = ItemProcsDeserializer::class)
    var procs: List<Proc>
): ModelBase() {
    val avgDmg: Int
        get() { return (minDmg + maxDmg) / 2}
}
