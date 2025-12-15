package mechanics

import character.Stats
import data.Constants
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import sim.EventResult
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
object Spell {
    private val logger = KotlinLogging.logger {}

    // https://wowwiki.fandom.com/wiki/Spell_hit
    val baseMissChance = mapOf(
        0 to 0.04,
        1 to 0.05,
        2 to 0.06,
        3 to 0.17
    )

    // https://www.youtube.com/watch?v=7DXvTVfDN18
    // Note that this cannot be reduced by spell pen or other mechanics
    val baseSpellResistance = mapOf(
        0 to 0,
        1 to 5,
        2 to 10,
        3 to 15
    )

    // Partial resists are calculated in terms of percentage of cap
    // The function is piecewise in thirds - each bracket scales linearly (but differently) between 0%, 33.3%, 66.6% and 100%
    // Weight of 0%, 25%, 50% and 75% partials across each of the three segments are tabulated below
    // https://royalgiraffe.github.io/partial-resist-table
    val resistWeights = mapOf(
        // Chance of 0% resist
        0.0 to listOf(
            // Avg res 0% to 33.3%
            Pair(1.0, 0.24),
            // Avg res 33.3% to 66.6%
            Pair(0.23, 0.0),
            // Avg res 66.6% to 100%
            Pair(0.0, 0.0)
        ),
        // Chance of 25% resist
        0.25 to listOf(
            // Avg res 0% to 33.3%
            Pair(0.0, 0.55),
            // Avg res 33.3% to 66.6%
            Pair(0.54, 0.21),
            // Avg res 66.6% to 100%
            Pair(0.20, 0.04)
        ),
        // Chance of 50% resist
        0.50 to listOf(
            // Avg res 0% to 33.3%
            Pair(0.0, 0.17),
            // Avg res 33.3% to 66.6%
            Pair(0.18, 0.56),
            // Avg res 66.6% to 100%
            Pair(0.57, 0.15)
        ),
        // Chance of 75% resist
        0.75 to listOf(
            // Avg res 0% to 33.3%
            Pair(0.0, 0.04),
            // Avg res 33.3% to 66.6%
            Pair(0.05, 0.22),
            // Avg res 66.6% to 100%
            Pair(0.23, 0.81)
        )
    )

    private fun <T> valueByLevelDiff(sp: SimParticipant, table: Map<Int, T>) : T {
        val levelDiff = sp.sim.target.character.level - sp.character.level

        return when {
            levelDiff <= 0 -> {
                logger.warn { "Attempted to compute a spell hit on a target more than 3 levels below" }
                table[0]!!
            }
            levelDiff > 3 -> {
                logger.warn { "Attempted to compute a spell hit on a target more than 3 levels above" }
                table[3]!!
            }
            else -> table[levelDiff]!!
        }
    }

    // AP and spell damage coefficients
    // https://wowwiki.fandom.com/wiki/Spell_power
    fun spellPowerCoeff(baseCastTimeMs: Int): Double {
        // Most instant spells are treated as 1.5s cast time for coeff purposes
        return baseCastTimeMs.coerceAtLeast(1500) / 3500.0
    }

    fun spellMissChance(sp: SimParticipant): Double {
        val baseMiss = valueByLevelDiff(sp, baseMissChance)
        val spellHitChance = sp.spellHitPct() / 100.0

        // Spells can never get to 100% hit kekw
        return (baseMiss - spellHitChance).coerceAtLeast(0.01)
    }

    // https://wow.gamepedia.com/Resistance
    // https://dwarfpriest.wordpress.com/2008/01/07/spell-hit-spell-penetration-and-resistances/
    // https://royalgiraffe.github.io/resist-guide
    fun spellResistReduction(sp: SimParticipant, school: Constants.DamageType, isBinary: Boolean): Double {
        val targetResistance = when(school) {
            Constants.DamageType.ARCANE -> sp.sim.target.stats.arcaneResistance
            Constants.DamageType.FIRE -> sp.sim.target.stats.fireResistance
            Constants.DamageType.FROST -> sp.sim.target.stats.frostResistance
            Constants.DamageType.NATURE -> sp.sim.target.stats.natureResistance
            Constants.DamageType.SHADOW -> sp.sim.target.stats.shadowResistance
            else -> 0
        }

        // theres a base 8 resist per level difference which cannot be negated by spellpen
        val baseResistance = (sp.sim.target.character.level - sp.character.level) * 8

        // Resistance brackets are a piecewise function which, when weighted, average to the target average resistance
        val resistCap = 5 * sp.character.level
        val totalResistance = ((targetResistance - sp.stats.spellPen).coerceAtLeast(0) + baseResistance).coerceAtMost(resistCap)

        if(isBinary) {
            // Binary spells are a boolean outcome based on average resistance
            val avgResistance = (0.75 * totalResistance / resistCap.toDouble()).coerceAtMost(0.75).coerceAtLeast(0.00)
            val fullResistRoll = Random.nextDouble()
            return if(fullResistRoll < avgResistance) {
                1.0
            } else {
                0.0
            }
        }

        // Compute partial resistances if not binary
        // https://royalgiraffe.github.io/partial-resist-table
        val pctOfCap = totalResistance / resistCap.toDouble()
        val oneThird = 1.0/3.0
        val twoThirds = 2.0/3.0

        // Find the probability of each quartile resistance based on the cap % and the function
        // First, find which reference line we are using based on how much resistance we have
        var lineIndex = 2
        var threshold = 1.0

        if(pctOfCap < oneThird) {
            lineIndex = 0
            threshold = oneThird
        } else if(pctOfCap < twoThirds) {
            lineIndex = 1
            threshold = twoThirds
        }

        val chanceByQuartileBase = resistWeights.map {
            // Then, find where we are on each line to get a percentage chance of this being the outcome
            val line = it.value[lineIndex]
            val weight = (pctOfCap / threshold).coerceAtMost(1.0)
            val totalChance = line.first * (1.0 - weight) + line.second * weight
            Pair(it.key, totalChance)
        }

        // To check against a single roll, each value needs to be the sum of the value before
        // The total sum of all chances is 1.0, and we're just making a simpler comparison
        val chanceByQuartile = chanceByQuartileBase.mapIndexed { idx, it ->
            Pair(it.first, chanceByQuartileBase.subList(0, idx + 1).sumOf { it.second })
        }

        val partialResistRoll = Random.nextDouble()
        return chanceByQuartile.first { partialResistRoll < it.second }.first
    }

    fun spellCritChance(sp: SimParticipant): Double {
        return (sp.spellCritPct() / 100.0).coerceAtLeast(0.0)
    }

    fun baseDamageRollSingle(sp: SimParticipant, baseDmg: Double, school: Constants.DamageType, spellDamageCoeff: Double = 1.0, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        // Add school damage
        val spellDamage = sp.spellDamageWithSchool(school)
        val totalSpellDamage = (spellDamage + bonusSpellDamage) * bonusSpellDamageMultiplier
        return baseDmg + (totalSpellDamage * spellDamageCoeff)
    }

    fun baseDamageRoll(sp: SimParticipant, minDmg: Double, maxDmg: Double, school: Constants.DamageType, spellDamageCoeff: Double = 1.0, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)
        val dmg = Random.nextDouble(min, max)
        return baseDamageRollSingle(sp, dmg, school, spellDamageCoeff, bonusSpellDamage, bonusSpellDamageMultiplier)
    }

    fun partialResistRoll(sp: SimParticipant, damageEvent: Pair<Double, EventResult>, school: Constants.DamageType): Pair<Double, EventResult> {
        val resistReduction = spellResistReduction(sp, school, false)
        val resistEventResult = when(resistReduction) {
            0.0 -> damageEvent.second
            else -> when(damageEvent.second) {
                EventResult.HIT -> EventResult.PARTIAL_RESIST_HIT
                EventResult.CRIT -> EventResult.PARTIAL_RESIST_CRIT
                else -> damageEvent.second
            }
        }

        return Pair(damageEvent.first * (1.0 - resistReduction), resistEventResult)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(
        sp: SimParticipant,
        damageRoll: Double,
        school: Constants.DamageType,
        isBinary: Boolean = false,
        bonusCritChance: Double = 0.0,
        bonusHitChance: Double = 0.0,
        bonusCritMultiplier: Double = 1.0,
        bonusDamageMultiplier: Double = 1.0,
        canCrit: Boolean = true,
        canResist: Boolean = true,
    ) : Pair<Double, EventResult> {
        val flatModifier = sp.stats.spellDamageFlatModifier
        val spellDamageMultiplier = sp.getSpellSchoolDamageMultiplier(school)

        val finalDamageRoll = (damageRoll + flatModifier) * spellDamageMultiplier * bonusDamageMultiplier

        val missChance = (spellMissChance(sp) - bonusHitChance).coerceAtLeast(0.01)
        val attackRoll = Random.nextDouble()

        // Get the hit/miss result
        var result = when {
            attackRoll < missChance && canResist -> Pair(0.0, EventResult.RESIST)
            else -> Pair(finalDamageRoll, EventResult.HIT)
        }

        if(result.second == EventResult.RESIST) return result

        if(canCrit) {
            val critChance = spellCritChance(sp) + bonusCritChance
            val critMultiplier =
                (Stats.spellCritMultiplier - 1.0) * (sp.stats.spellDamageAddlCritMultiplier) * bonusCritMultiplier + 1
            val hitRoll2 = Random.nextDouble()

            if(hitRoll2 < critChance) {
                result = Pair(result.first * critMultiplier, EventResult.CRIT)
            }
        }

        if(canResist && !isBinary) {
            return partialResistRoll(sp, result, school)
        }

        return result
    }
}
