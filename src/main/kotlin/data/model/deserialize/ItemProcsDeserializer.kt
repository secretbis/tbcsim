package data.model.deserialize

import character.Proc
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class ItemProcsDeserializer : JsonDeserializer<Proc>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Proc {
        TODO()
    }
}
