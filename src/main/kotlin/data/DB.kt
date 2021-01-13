package data

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.model.Item
import data.model.ModelBase
import data.model.Spell
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

class DB (val items: Map<Int, Item>, val itemsList: List<Item>, val spells: Map<Int, Spell>, val spellsList: List<Spell>) {
    companion object {
        private val mapper = ObjectMapper().registerKotlinModule()

        private fun <T : ModelBase> load(file: String, type: TypeReference<T>): Pair<Map<Int, T>, List<T>> {
            try {
                // Load
                val data = File(DB::class.java.getResource(file).toURI()).readText()
                val parsed: List<T> = mapper.readValue(data)

                // Transform to map
                val asMap: Map<Int, T> = parsed.map { it.id to it }.toMap()

                return Pair(asMap, parsed)
            } catch (e: Exception) {
                logger.error(e) { "Failed to load database: $file" }
                return Pair(mapOf(), listOf())
            }
        }

        fun init(): DB {
            val items = load("items.json", object : TypeReference<Item>(){})
            val spells = load("spells.json", object : TypeReference<Spell>(){})
            return DB(items.first, items.second, spells.first, spells.second)
        }
    }
}
