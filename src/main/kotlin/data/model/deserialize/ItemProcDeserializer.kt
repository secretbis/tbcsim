package data.model.deserialize

import character.Proc
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import data.model.ItemProc
import data.model.default.UnknownProc

class ItemProcDeserializer : JsonDeserializer<ItemProc>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ItemProc {
        val node = p!!.codec.readTree<JsonNode>(p)
        val itemProc = ItemProc()

        // Set basic fields
        itemProc.id = node.get("Id").asInt(itemProc.id)
        itemProc.name = node.get("SpellName").asText(itemProc.name)

        // Check to see if we can implement the proc


        return itemProc
    }

    fun createProcForId(id: Int, name: String): Proc {
        return UnknownProc()
    }
}
