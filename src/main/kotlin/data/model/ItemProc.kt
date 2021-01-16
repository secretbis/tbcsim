package data.model

import character.Proc
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.model.default.UnknownProc
import data.model.deserialize.ItemProcDeserializer

@JsonDeserialize(using = ItemProcDeserializer::class)
open class ItemProc : ModelBase() {
    var name: String = "Unknown"
    var proc: Proc = UnknownProc(-1, "Default", -1, "Default")
}
