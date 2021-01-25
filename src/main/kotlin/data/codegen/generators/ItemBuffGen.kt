package data.codegen.generators

import com.fasterxml.jackson.core.type.TypeReference
import com.fleshgrinder.extensions.kotlin.toUpperCamelCase
import com.squareup.kotlinpoet.CodeBlock
import data.buffs.Buffs
import data.buffs.GenericArmorPenBuff
import data.buffs.GenericAttackPowerBuff
import data.buffs.GenericRangedAttackPowerBuff
import data.codegen.CodeGen
import data.default.UnknownBuff
import mu.KotlinLogging

object ItemBuffGen {
    private val logger = KotlinLogging.logger {}

    private val mappersByName = mapOf(
        Regex("Armor Penetration \\d+") to fun (name: String): CodeBlock {
            val armorPen = name.drop(18).toInt()

            return CodeBlock.builder()
                .add("%T(%L)", GenericArmorPenBuff::class, armorPen)
                .build()
        },
        Regex("Attack Power \\d+") to fun (name: String): CodeBlock {
            val armorPen = name.drop(13).toInt()

            return CodeBlock.builder()
                .add("%T(%L)", GenericAttackPowerBuff::class, armorPen)
                .build()
        },
        Regex("Attack Power Ranged \\d+") to fun (name: String): CodeBlock {
            val armorPen = name.drop(20).toInt()

            return CodeBlock.builder()
                .add("%T(%L)", GenericRangedAttackPowerBuff::class, armorPen)
                .build()
        }
    )

    private fun buffBlockFor(id: Int, name: String): CodeBlock {
        // Check by name for buffs where the name entirely describes what it does
        for(mapper in mappersByName) {
            if(mapper.key.matches(name)) {
                return mapper.value(name)
            }
        }

        // Check by ID in case there are ambiguous names
        val buff = Buffs.byId(id)
        if(buff != null) {
            return CodeBlock.builder()
                .add("%T()", buff::class)
                .build()
        }

        logger.warn("Unable to map buff name to buff implementation: $name")
        return CodeBlock.builder()
            .add("%T()", UnknownBuff::class)
            .build()
    }

    private fun load(): List<Map<String, Any?>> {
        return CodeGen.load("/itemprocs.json", object : TypeReference<List<Map<String, Any?>>>(){})
    }

    fun generate(): Map<Int, CodeBlock> {
        val itemBuffs = load()

        return itemBuffs.associate {
            // Set basic fields
            val id = it["Id"] as Int
            val name = it["SpellName"] as String? ?: "Unknown$id"
            val cleanName = name.replace(Regex("""^[a-zA-Z ]"""), "").toUpperCamelCase()

            // Check to see if we can implement the proc
            val buffBlock = buffBlockFor(id, name)

            Pair(id, buffBlock)
        }
    }
}
