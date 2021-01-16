package data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.model.deserialize.SpellDeserializer

@JsonIgnoreProperties(ignoreUnknown = true)
open class ItemProc (
    @JsonProperty("Id")
    override var id: Int,
    @JsonProperty("SpellName")
    var name: String,
    @JsonDeserialize(using = SpellDeserializer::class)
    var spell: ItemProc
) : ModelBase()
