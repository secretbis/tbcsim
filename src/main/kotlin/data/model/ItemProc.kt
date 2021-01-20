package data.model

import character.Proc
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import data.model.default.UnknownProc
import data.model.deserialize.ItemProcDeserializer

// This class functions as deserialization temporary storage, and shouldn't be used elsewhere
@JsonDeserialize(using = ItemProcDeserializer::class)
open class ItemProc : ModelBase() {
    var name: String = "Unknown"
    var proc: Proc = UnknownProc()
}
