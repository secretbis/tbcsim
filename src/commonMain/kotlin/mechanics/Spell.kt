package mechanics

import character.Stats
import data.Constants
import mu.KotlinLogging
import sim.Event
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

    /**
     * First roll for damage and miss roll calculation
     * 
     * @param spellDamageRoll Roll based on spell needs. 
     * Use result from `baseDamageRoll` for spells with base damage range pairs (e.g. 500 to 575)
     * Use result from `baseDamageRollFromSnapShot` for spells which store spell damage for later (e.g. Spell Dots)
     * Use result from `baseDamageRollSingle` for spells which do not have a range of damage
     * @param school Spell school for retrieving additional damage based on school and multipliers
     * @param bonusHitChance Provide more hit chance to landing spell outside the casters spell hit
     * @param canResist Can this attack be resisted
     * 
     * @return Pair<Double, EventResult> where first is the damage to be done and second is event of HIT (success) or RESIST (failure)
     */
    private fun firstAttackRollPair(sp: SimParticipant, spellDamageRoll: Double, school: Constants.DamageType, bonusHitChance: Double = 0.0, canResist: Boolean = true) : Pair<Double, EventResult> {
        // Additional damage multipliers
        val flatModifier = sp.stats.spellDamageFlatModifier
        val spellDamageMultiplier = sp.getSpellDamageMultiplier(school)

        val finalDamageRoll = (spellDamageRoll + flatModifier) * spellDamageMultiplier

        // Get the hit/miss result
        if (canResist){
            val missChance = (spellMissChance(sp) - bonusHitChance).coerceAtLeast(0.01)
            val attackRoll = Random.nextDouble()

            return when {
                attackRoll < missChance -> Pair(0.0, EventResult.RESIST)
                else -> Pair(finalDamageRoll, EventResult.HIT)
            }
        }

        return Pair(finalDamageRoll, EventResult.HIT);
    }

    /**
     * Second roll for spell crit based calculation
     * 
     * @param result Result Pair from first roll
     * @param bonusCritChance Additional chance to crit
     * @param bonusCritMultiplier Additional damage if a crit does occur
     * 
     * @return Pair<Double, EventResult> where first is the damage to be done and second is event of CRIT if successful
     */
    private fun secondCritRollPair(sp: SimParticipant, result: Pair<Double, EventResult>, bonusCritChance: Double = 0.0, bonusCritMultiplier: Double = 1.0): Pair<Double, EventResult> {
        // Find all our possible damage mods from buffs and so on
        val critChance = spellCritChance(sp) + bonusCritChance
        val critMultiplier = (Stats.spellCritMultiplier - 1.0) * (sp.stats.spellDamageAddlCritMultiplier) * bonusCritMultiplier + 1
        val hitRoll2 = Random.nextDouble()

        if (hitRoll2 < critChance){
            return Pair(result.first * critMultiplier, EventResult.CRIT)
        }
        
        return result;
    }

    /**
     * Third and final roll (For spells which can resist) for spell resist based calculation
     * 
     * @param result Result Pair from first or second roll
     * @param school Spell school for retrieving resistence 
     * @param isBinary Additional damage if a crit does occur
     * 
     * @return Pair<Double, EventResult> where first is the damage after resistance calculations and event result remains the same
     */
    private fun thirdResistRollPair(sp: SimParticipant, result: Pair<Double, EventResult>, school: Constants.DamageType, isBinary: Boolean): Pair<Double, EventResult> {
        val resistAvgReduction = spellResistReduction(sp, school)

        if(isBinary) {
            // Make a third roll for full resist or not
            val fullResistRoll = Random.nextDouble()
            val fullResistMod = if(fullResistRoll < resistAvgReduction) { 0.0 } else { 1.0 }
            return Pair(result.first * fullResistMod, result.second)
        } else {
            return Pair(result.first * (1 - resistAvgReduction), result.second)
        }
    }

    // AP and spell damage coefficients
    // TODO: Verify that these formulas reflect TBC mechanics
    // https://wowwiki.fandom.com/wiki/Spell_power
    fun spellPowerCoeff(baseCastTimeMs: Int, baseDurationMs: Int = 0): Double {
        // DoT
        return if(baseDurationMs == 0) {
            // Most instant spells are treated as 1.5s cast time for coeff purposes
            baseCastTimeMs.coerceAtLeast(1500) / 3500.0
        } else {
            baseDurationMs / 15000.0
        }
    }

    fun spellMissChance(sp: SimParticipant): Double {
        val baseMiss = valueByLevelDiff(sp, baseMissChance)
        val spellHitChance = sp.spellHitPct() / 100.0

        // Spells can never get to 100% hit kekw
        return (baseMiss - spellHitChance).coerceAtLeast(0.01)
    }

    // https://wow.gamepedia.com/Resistance
    // https://dwarfpriest.wordpress.com/2008/01/07/spell-hit-spell-penetration-and-resistances/
    fun spellResistReduction(sp: SimParticipant, school: Constants.DamageType): Double {
        val targetResistance = when(school) {
            Constants.DamageType.ARCANE -> sp.sim.target.stats.arcaneResistance
            Constants.DamageType.FIRE -> sp.sim.target.stats.fireResistance
            Constants.DamageType.FROST -> sp.sim.target.stats.frostResistance
            Constants.DamageType.NATURE -> sp.sim.target.stats.natureResistance
            Constants.DamageType.SHADOW -> sp.sim.target.stats.shadowResistance
            else -> 0
        }

        // theres a base 8 resist per level which can not be negated by spellpen
        val baseResistance = (sp.sim.target.character.level - sp.character.level) * 8

        // TODO: Model partial resists as 0/25/50/75
        //       There seems to be no real formula for that, though, so just going with avg every time for now
        val totalResistance = (targetResistance - sp.stats.spellPen).coerceAtLeast(0) + baseResistance
        return (0.75 * totalResistance / (5 * sp.character.level.toDouble())).coerceAtMost(0.75).coerceAtLeast(0.00)
    }

    fun spellCritChance(sp: SimParticipant): Double {
        return (sp.spellCritPct() / 100.0).coerceAtLeast(0.0)
    }

    fun baseDamageRollFromSnapShot(baseDmg: Double, spellDamage: Double, spellDamageCoeff: Double = 1.0): Double {
        return baseDmg + (spellDamage * spellDamageCoeff)
    }

    fun baseDamageRollSingle(sp: SimParticipant, baseDmg: Double, school: Constants.DamageType, spellDamageCoeff: Double = 1.0, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        // Add school damage
        val spellDamage = sp.spellDamageWithSchool(school)
        val totalSpellDamage = (spellDamage + bonusSpellDamage) * bonusSpellDamageMultiplier
        return baseDamageRollFromSnapShot(baseDmg, totalSpellDamage, spellDamageCoeff)
    }

    fun baseDamageRoll(sp: SimParticipant, minDmg: Double, maxDmg: Double, school: Constants.DamageType, spellDamageCoeff: Double = 1.0, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)
        val dmg = Random.nextDouble(min, max)
        return baseDamageRollSingle(sp, dmg, school, spellDamageCoeff, bonusSpellDamage, bonusSpellDamageMultiplier)
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
        canCrit: Boolean = true,
        canResist: Boolean = true,
    ) : Pair<Double, EventResult> {
        var finalResult = firstAttackRollPair(sp, damageRoll, school, bonusHitChance, canResist)

        if(canCrit && finalResult.second == EventResult.HIT) {
            finalResult = secondCritRollPair(sp, finalResult, bonusCritChance, bonusCritMultiplier)
        }

        if(!canResist){
            finalResult = thirdResistRollPair(sp,finalResult, school, isBinary)
        }

        return finalResult
    }
}
