package mechanics

import character.Stats
import data.Constants
import mu.KotlinLogging
import sim.Event
import sim.SimIteration
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

        // TODO: Model partial resists as 0/25/50/75
        //       There seems to be no real formula for that, though, so just going with avg every time for now
        val totalResistance = (targetResistance - sp.stats.spellPen).coerceAtLeast(0) + targetResistance
        return (0.75 * totalResistance / (5 * sp.character.level.toDouble())).coerceAtMost(0.75).coerceAtLeast(0.00)
    }

    fun spellSchoolDamageMultiplier(sp: SimParticipant, school: Constants.DamageType): Double {
        return when(school) {
            Constants.DamageType.ARCANE -> sp.stats.arcaneDamageMultiplier
            Constants.DamageType.FIRE -> sp.stats.fireDamageMultiplier
            Constants.DamageType.FROST -> sp.stats.frostDamageMultiplier
            Constants.DamageType.NATURE -> sp.stats.natureDamageMultiplier
            Constants.DamageType.SHADOW -> sp.stats.shadowDamageMultiplier
            else -> 1.0
        }
    }

    fun spellCritChance(sp: SimParticipant): Double {
        return (sp.spellCritPct() / 100.0).coerceAtLeast(0.0)
    }

    fun baseDamageRoll(sp: SimParticipant, minDmg: Double, maxDmg: Double, spellDamageCoeff: Double = 1.0, school: Constants.DamageType, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)
        val dmg = Random.nextDouble(min, max)
        return baseDamageRollSingle(sp, dmg, spellDamageCoeff, school, bonusSpellDamage, bonusSpellDamageMultiplier)
    }

    fun baseDamageRollSingle(sp: SimParticipant, dmg: Double, spellDamageCoeff: Double = 1.0, school: Constants.DamageType, bonusSpellDamage: Int = 0, bonusSpellDamageMultiplier: Double = 1.0): Double {
        // Add school damage
        val schoolDamage = when(school) {
            Constants.DamageType.HOLY -> sp.stats.holyDamage
            Constants.DamageType.FIRE -> sp.stats.fireDamage
            Constants.DamageType.NATURE -> sp.stats.natureDamage
            Constants.DamageType.FROST -> sp.stats.frostDamage
            Constants.DamageType.SHADOW -> sp.stats.shadowDamage
            Constants.DamageType.ARCANE -> sp.stats.arcaneDamage
            else -> 0
        }

        val totalSpellDamage = (sp.spellDamage() + bonusSpellDamage + schoolDamage) * bonusSpellDamageMultiplier
        return dmg + (totalSpellDamage * spellDamageCoeff)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sp: SimParticipant, damageRoll: Double, school: Constants.DamageType, isBinary: Boolean = false, bonusCritChance: Double = 0.0, bonusHitChance: Double = 0.0) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val critMultiplier = Stats.spellCritMultiplier + (sp.stats.spellDamageAddlCritMultiplier - 1)

        // School damage multiplier
        val schoolDamageMultiplier = spellSchoolDamageMultiplier(sp, school)

        // Additional damage multipliers
        val flatModifier = sp.stats.spellDamageFlatModifier
        val allMultiplier = sp.stats.spellDamageMultiplier

        val finalDamageRoll = (damageRoll + flatModifier) * allMultiplier * schoolDamageMultiplier

        // Get the attack result
        val missChance = (spellMissChance(sp) - bonusHitChance).coerceAtLeast(0.0)
        val critChance = spellCritChance(sp) + bonusCritChance

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, Event.Result.RESIST)
            else -> Pair(finalDamageRoll, Event.Result.HIT)
        }

        // Two-roll all spells
        if(finalResult.second == Event.Result.HIT) {
            val hitRoll2 = Random.nextDouble()
            finalResult = when {
                hitRoll2 < critChance -> Pair(
                    finalResult.first * critMultiplier,
                    Event.Result.CRIT
                )
                else -> finalResult
            }
        }

        // Apply resistance mitigation
        val resistAvgReduction = spellResistReduction(sp, school)
        if(isBinary) {
            // Make a third roll for full resist or not
            val fullResistRoll = Random.nextDouble()
            val fullResistMod = if(fullResistRoll < resistAvgReduction) { 0.0 } else { 1.0 }
            finalResult = Pair(finalResult.first * fullResistMod, finalResult.second)
        } else {
            finalResult = Pair(finalResult.first * (1 - resistAvgReduction), finalResult.second)
        }

        return finalResult
    }
}
