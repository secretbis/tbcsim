package data.model.deserialize

import character.Stats
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import data.Constants.StatType

class ItemStatsDeserializer : JsonDeserializer<Stats>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Stats {
        val node = p!!.codec.readTree<JsonNode>(p)

        val stats = Stats()
        for(i in 1..5) {
            val type = node.get("stat_type${i}")
            val value = node.get("stat_value${i}").asInt(0)

            val enumType = StatType.valueOf(type.asText() ?: "0")

            if(enumType != StatType.NONE) {
                when(enumType) {
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
        }

        return stats
    }
}
