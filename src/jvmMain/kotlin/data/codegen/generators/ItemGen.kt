@file:Suppress("UNUSED_PARAMETER")

package data.codegen.generators

import character.Stats
import com.fasterxml.jackson.core.type.TypeReference
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import data.Constants
import data.buffs.Buffs
import data.codegen.CodeGen
import data.itemscustom.EmptyItem
import data.itemsets.ItemSets
import data.model.*
import data.socketbonus.SocketBonuses
import mu.KotlinLogging
import net.pearx.kasechange.toPascalCase
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.reflect.Modifier

object ItemGen {
    val logger = KotlinLogging.logger {}
    val pkg: String = "data.items"

    val itemOverrides = mapOf(
        32837 to mapOf(
            "className" to "WarglaiveOfAzzinothMH",
            "name" to "Warglaive of Azzinoth (MH)"
        ),
        32838 to mapOf(
            "className" to "WarglaiveOfAzzinothOH",
            "name" to "Warglaive of Azzinoth (OH)"
        )
    )

    val itemIgnore = listOf(
        25968   // Shalassi Sentry's Epaulets
    )

    private fun load(): List<Map<String, Any?>> {
        return CodeGen.load("/items.json", object : TypeReference<List<Map<String, Any?>>>(){})
    }

    private fun loadBuffs(): List<Map<String, Any?>> {
        return CodeGen.load("/itemprocs.json", object : TypeReference<List<Map<String, Any?>>>(){})
    }

    private fun loadIcons(): Map<String, String?> {
        return CodeGen.load("/item_icons.json", object : TypeReference<Map<String, String?>>(){})
    }

    private fun deserializeSockets(itemData: Map<String, Any?>): Array<Socket> {
        return (1..3).mapNotNull { i ->
            val socketColor = itemData["socketColor_$i"] as Int
            if(socketColor == 0) {
                null
            } else {
                val color = Color.values().find { it.mask == socketColor }
                if (color != null) {
                    Socket(color)
                } else {
                    logger.warn { "Could not interpret socket color value: $color" }
                    null
                }
            }
        }.toTypedArray()
    }

    private fun deserializeStats(itemData: Map<String, Any?>) : Stats {
        val stats = Stats(armor = (itemData["armor"] as Int? ?: 0))
        for(i in 1..5) {
            val type = itemData["stat_type$i"] as Int? ?: 0
            val value = itemData["stat_value$i"] as Int? ?: 0
            val statType = Constants.StatType.values().find { it() == type }!!

            stats.addByStatType(statType, value)
        }
        return stats
    }

    fun generate() {
        val itemsData = load()
        val itemBuffsData = loadBuffs()
        val itemIcons = loadIcons()

        val protoItems = itemsData.map {
            val item = EmptyItem()

            item.id = it["entry"] as Int? ?: item.id

            val nameOverride = itemOverrides[item.id]?.get("name")
            item.name = nameOverride ?: it["name"] as String? ?: item.name
            item.itemLevel = it["ItemLevel"] as Int? ?: item.itemLevel
            item.quality = it["Quality"] as Int? ?: item.quality
            item.inventorySlot = it["InventoryType"] as Int? ?: item.inventorySlot
            item.icon = itemIcons[item.id.toString()] ?: item.icon
            item.itemClass = Constants.ItemClass.values().find { it2 ->
                it2.ordinal == it["class"] as Int?
            }
            item.itemSubclass = Constants.ItemClass.subclasses(item.itemClass).find { it2 ->
                it2.itemClassOrdinal == it["subclass"] as Int?
            }
            item.minDmg = (it["dmg_min1"] as Number? ?: item.minDmg).toDouble()
            item.maxDmg = (it["dmg_max1"] as Number? ?: item.maxDmg).toDouble()
            item.speed = (it["delay"] as Number? ?: item.speed).toDouble()
            item.stats = deserializeStats(it)
            item.sockets = deserializeSockets(it)

            Pair(item, it)
        }.filter { !itemIgnore.contains(it.first.id) }

        // Write individual item files
        protoItems.forEach { itemPair ->
            writeItemClassFile(itemPair.first, itemPair.second, itemBuffsData)
        }

        // Write index file
        writeItemIndexFile(protoItems.map { it.first })
    }

    private fun writeItemIndexFile(items: List<Item>) {
        val objBuilder = TypeSpec.objectBuilder("ItemIndex")

        val itemsListBlock = items.map {
            CodeBlock.of("{ %L() }", safeItemName(it))
        }.joinToCode(separator = ",\n", prefix = "arrayOf(\n", suffix = "\n)\n")

        objBuilder.addProperty(
            PropertySpec.builder("items", ARRAY.parameterizedBy(LambdaTypeName.get(returnType = ClassName("data.model", "Item"))))
                .initializer("%L", itemsListBlock)
                .build()
        )

        FileSpec.builder(pkg, "ItemIndex")
            .addType(objBuilder.build())
            .build()
            .writeTo(File(CodeGen.outPath))
    }

    private fun shouldOverwrite(name: String): Boolean {
        return try {
            // Check to see if the class exists and the autoGenerated field is set
            // If it has been manually changed and isAutogenerated is false, then do not overwrite
            val klass = Class.forName("data.items.$name")
            return klass.getDeclaredMethod("isAutoGenerated").invoke(klass.getDeclaredConstructor().newInstance()) as Boolean
        } catch(e: ClassNotFoundException) {
            true
        }
    }

    private fun renderStats(item: Item, itemData: Map<String, Any?>): CodeBlock {
        val fields = Class.forName("character.Stats").declaredFields
            .filter { it.name != "Companion" }
            .filter { !it.name.startsWith("$") }
            .filter { !Modifier.isFinal(it.modifiers) }

        // Render all stats constructor params and inject Item values
        val baseStats = Stats()
        val block = fields.mapNotNull {
            val method = Class.forName("character.Stats").getDeclaredMethod("get${it.name.capitalize()}")
            val methodValue = method.invoke(item.stats)

            // Only render non-default values
            if(methodValue == method.invoke(baseStats)) {
                null
            } else {
                CodeBlock.of("%L = %L", it.name, methodValue)
            }
        }.joinToCode(separator = ",\n", prefix = "(\n", suffix = "\n)")

        return CodeBlock.builder()
            .add("%T", Stats::class)
            .add(block)
            .build()
    }

    private fun renderSockets(item: Item, itemData: Map<String, Any?>): CodeBlock {
        return if(item.sockets.isNotEmpty()) {
            item.sockets.map {
                CodeBlock.of("%1T(%2T.%3L)", Socket::class, Color::class, it.color)
            }.joinToCode(separator = ",\n", prefix = "arrayOf(\n", suffix = "\n)")
        } else {
            CodeBlock.builder()
                .add("%L", "arrayOf()")
                .build()
        }
    }

    private fun renderSocketBonus(item: Item, itemData: Map<String, Any?>): CodeBlock {
        val bonusId = itemData["socketBonus"] as Int?
        return if(bonusId != null && bonusId != 0) {
            CodeBlock.of("%T.byId(%L)", SocketBonuses::class, bonusId)
        } else {
            CodeBlock.of("%L", null)
        }
    }

    private fun renderItemSet(item: Item, itemData: Map<String, Any?>): CodeBlock {
        val itemSet = itemData["itemset"] as Int?
        return if(itemSet != null && itemSet != 0) {
            CodeBlock.of("%T.byId(%L)", ItemSets::class, itemSet)
        } else {
            CodeBlock.of("%L", null)
        }
    }

    private fun renderItemClass(item: Item, itemData: Map<String, Any?>): CodeBlock {
        return if(item.itemClass != null) {
            CodeBlock.of("%T.%L", Constants.ItemClass::class, item.itemClass)
        } else {
            CodeBlock.of("%L", null)
        }
    }

    private fun renderItemSubclass(item: Item, itemData: Map<String, Any?>): CodeBlock {
        return if(item.itemSubclass != null) {
            CodeBlock.of("%T.%L", Constants.ItemSubclass::class, item.itemSubclass)
        } else {
            CodeBlock.of("%L", null)
        }
    }

    private fun renderBuffs(item: Item, itemData: Map<String, Any?>, itemBuffsData: List<Map<String, Any?>>): CodeBlock {
        val buffIds = (1..5).mapNotNull { i ->
            val spellId = itemData["spellid_$i"] as Int? ?: 0

            if(spellId == 0) {
                null
            } else {
                // Find buff name and ID from DB
                val itemBuff = itemBuffsData.find { it["Id"] as Int == spellId }
                    ?: throw IllegalArgumentException("Cannot find item buff with ID: $spellId")

                val name = itemBuff["SpellName"] as String
                Pair(spellId, name)
            }
        }

        return if(buffIds.isNotEmpty()) {
            buffIds.map {
                CodeBlock.of("%T.byIdOrName(%L, %S, %L)", Buffs::class, it.first, it.second, "this")
            }.joinToCode(separator = ",\n", prefix = "listOfNotNull(\n", suffix = "\n)")
        } else {
            CodeBlock.builder()
                .add("%L", "listOf()")
                .build()
        }
    }

    private fun renderAllowableClasses(item: Item, itemData: Map<String, Any?>): CodeBlock {
        val classMask = itemData["AllowableClass"] as Int?
        if(classMask != null && classMask != -1) {
            val classes = Constants.AllowableClass.values().filter {
                it() and classMask != 0
            }

            return classes.map {
                CodeBlock.of("%T.%L", Constants.AllowableClass::class, it.name)
            }.joinToCode(separator = ",\n", prefix = "arrayOf(\n", suffix = "\n)")
        } else {
            return CodeBlock.of("%L", null)
        }
    }

    fun safeItemName(item: Item): String {
        if(itemOverrides[item.id] != null) {
            return itemOverrides[item.id]!!["className"]!!
        }
        val safeRegex = Regex("""[^a-zA-Z ]""")
        return item.name.replace(safeRegex, "").toPascalCase()
    }

    fun writeItemClassFile(item: Item, itemData: Map<String, Any?>, itemBuffsData: List<Map<String, Any?>>) {
        val className = safeItemName(item)

        if(shouldOverwrite(className)) {
            val file = FileSpec.builder(pkg, className)
                .addType(
                    TypeSpec.classBuilder(className)
                        .superclass(Item::class)
                        .addAnnotation(
                            AnnotationSpec.builder(
                                ClassName.bestGuess("kotlin.js.JsExport")
                            ).build()
                        )
                        .addProperty(
                            PropertySpec.builder("isAutoGenerated", Boolean::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", true)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("id", Int::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.id)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("name", String::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%S", item.name)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("itemLevel", Int::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.itemLevel)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("quality", Int::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.quality)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("icon", String::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%S", item.icon)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("inventorySlot", Int::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.inventorySlot)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("itemSet", ItemSet::class.asTypeName().copy(true))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderItemSet(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("itemClass", Constants.ItemClass::class.asTypeName().copy(true))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderItemClass(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("itemSubclass", Constants.ItemSubclass::class.asTypeName().copy(true))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderItemSubclass(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("allowableClasses",  Array::class.asTypeName().parameterizedBy(Constants.AllowableClass::class.asTypeName()).copy(true))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderAllowableClasses(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("minDmg", Double::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.minDmg)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("maxDmg", Double::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.maxDmg)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("speed", Double::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", item.speed)
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("stats", Stats::class)
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderStats(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("sockets", ARRAY.parameterizedBy(ClassName("data.model", "Socket")))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderSockets(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("socketBonus", SocketBonus::class.asTypeName().copy(true))
                                .addModifiers(KModifier.OVERRIDE)
                                .mutable(true)
                                .initializer("%L", renderSocketBonus(item, itemData))
                                .build()
                        )
                        .addProperty(
                            PropertySpec.builder("buffs", LIST.parameterizedBy(ClassName("character", "Buff")))
                                .addModifiers(KModifier.OVERRIDE)
                                .delegate(
                                    CodeBlock.builder()
                                        .beginControlFlow("lazy")
                                        .add(renderBuffs(item, itemData, itemBuffsData))
                                        .endControlFlow()
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()

            file.writeTo(File(CodeGen.outPath))
        }
    }
}
