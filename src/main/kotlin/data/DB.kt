package data

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.codegen.ItemGen
import data.model.Item
import data.model.ItemProc
import data.model.ModelBase
import data.socketbonus.SocketBonusRaw
import data.socketbonus.allSocketBonusesRaw

object DB {
    var items: Map<Int, Item> = mapOf()
    var itemsList: List<Item> = listOf()
    var itemProcs: Map<Int, ItemProc> = mapOf()
    var itemProcsList: List<ItemProc> = listOf()
    var socketBonusesRaw: Map<Int, SocketBonusRaw> = mapOf()
    var socketBonusesListRaw: List<SocketBonusRaw> = listOf()

    private val mapper = ObjectMapper().registerKotlinModule()

    private fun <T : ModelBase> load(file: String, type: TypeReference<List<T>>): Pair<Map<Int, T>, List<T>> {
        // Load
        val data = DB::class.java.getResourceAsStream(file).readAllBytes().decodeToString()
        val parsed = mapper.readValue(data, type)

        // Transform to map
        val asMap: Map<Int, T> = parsed.map { it.id to it }.toMap()

        return Pair(asMap, parsed)
    }

    fun writeAllItemsToFile() {
        for(item in itemsList) {
            ItemGen.writeItemClassFile(item)
        }
    }

    fun init() {
        // Socket bonuses are already in KT form
        this.socketBonusesListRaw = allSocketBonusesRaw
        this.socketBonusesRaw = socketBonusesListRaw.groupBy { it.id }.mapValues { it.value.first() }

        // This order is important, since items need to lookup itemProcs
        val itemProcs = load("/itemprocs.json", object : TypeReference<List<ItemProc>>(){})
        this.itemProcs = itemProcs.first
        this.itemProcsList = itemProcs.second

        val items = load("/items.json", object : TypeReference<List<Item>>(){})
        this.items = items.first
        this.itemsList = items.second

        writeAllItemsToFile()
    }
}
