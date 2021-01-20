package data.model.deserialize

import character.Proc
import character.Stats
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import data.Constants.StatType
import data.DB
import data.model.Item
import mu.KotlinLogging

class ItemDeserializer : JsonDeserializer<Item>() {
    private val logger = KotlinLogging.logger {}

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Item {
        val node = p!!.codec.readTree<JsonNode>(p)

        val item = Item()
        item.id = node.get("entry").asInt(item.id)
        item.name = node.get("name").asText(item.name)
        item.itemLevel = node.get("ItemLevel").asInt(item.itemLevel)
        item.minDmg = node.get("dmg_min1").asDouble(item.minDmg)
        item.maxDmg = node.get("dmg_max1").asDouble(item.maxDmg)
        item.speed = node.get("delay").asDouble(item.speed)
        item.stats = deserializeStats(node)

        item.procs = deserializeProcs(node)

        return item
    }

    private fun deserializeProcs(node: JsonNode): List<Proc> {
        val procs: MutableList<Proc> = mutableListOf()
        for(i in 1..5) {
            val spellId = node.get("spellid_$i").asInt(0)

            if(spellId == 0) continue

            val itemProc = DB.itemProcs[spellId]

            if(itemProc == null) {
                logger.warn("Unextracted spell ID: $spellId")
                continue
            }

            // TODO: Are these relevant for item procs?  Maybe?
//            val spellPpm = node.get("spellppmrate_$i").asInt(0)
//            val spellCooldown = node.get("spellcooldown_$i")
//            var spellCategoryCooldown = node.get("spellcategorycooldown_$i")

            procs.add(itemProc.proc)
        }

        return procs
    }

    private fun deserializeStats(node: JsonNode) : Stats {
        val stats = Stats()
        for(i in 1..5) {
            val type = node.get("stat_type$i").asInt(0)
            val value = node.get("stat_value$i").asInt(0)

            val enumType = StatType.values().find { it() == type }

            when (enumType) {
                StatType.AGILITY ->
                    stats.agility = stats.agility + value
                StatType.CRIT_MELEE_RATING, StatType.CRIT_RANGED_RATING ->
                    stats.physicalCritRating = stats.physicalCritRating + value
                StatType.CRIT_RATING -> {
                    stats.physicalCritRating = stats.physicalCritRating + value
                    stats.spellCritRating = stats.spellCritRating + value
                }
                StatType.CRIT_SPELL_RATING ->
                    stats.spellCritRating = stats.spellCritRating + value
                StatType.EXPERTISE_RATING ->
                    stats.expertiseRating = stats.expertiseRating + value
                StatType.HASTE_MELEE_RATING, StatType.HASTE_RANGED_RATING ->
                    stats.physicalHasteRating = stats.physicalHasteRating + value
                StatType.HASTE_RATING -> {
                    stats.physicalHasteRating = stats.physicalHasteRating + value
                    stats.spellHasteRating = stats.spellHasteRating + value
                }
                StatType.HASTE_SPELL_RATING ->
                    stats.spellHasteRating = stats.spellHasteRating + value
                StatType.HIT_MELEE_RATING, StatType.HIT_RANGED_RATING ->
                    stats.physicalHitRating = stats.physicalHitRating + value
                StatType.HIT_RATING -> {
                    stats.physicalHitRating = stats.physicalHitRating + value
                    stats.spellHitRating = stats.spellHitRating + value
                }
                StatType.HIT_SPELL_RATING -> {
                    stats.spellHitRating = stats.spellHitRating + value
                }
                StatType.INTELLECT ->
                    stats.intellect = stats.intellect + value
                StatType.SPIRIT ->
                    stats.spirit = stats.spirit + value
                StatType.STAMINA ->
                    stats.stamina = stats.stamina + value
                StatType.STRENGTH ->
                    stats.strength = stats.strength + value
                else -> {
                    // Do nothing
                }
            }
        }
        return stats
    }
}
