package data

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.model.Item
import data.model.ModelBase
import data.model.ItemProc
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}

object DB {
    var items: Map<Int, Item> = mapOf()
    var itemsList: List<Item> = listOf()
    var spells: Map<Int, ItemProc> = mapOf()
    var spellsList: List<ItemProc> = listOf()

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

    fun init() {
        // This order is important, since items need to lookup spells
        val spells = load("itemprocs.json", object : TypeReference<ItemProc>(){})
        this.spells = spells.first
        this.spellsList = spells.second

        val items = load("items.json", object : TypeReference<Item>(){})
        this.items = items.first
        this.itemsList = items.second
    }
}
