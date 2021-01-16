package data.model.deserialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import data.model.ItemProc

class SpellDeserializer : JsonDeserializer<ItemProc>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ItemProc {
        TODO()
    }
}
