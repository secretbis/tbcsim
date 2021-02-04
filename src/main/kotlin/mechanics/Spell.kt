package mechanics

import character.Stats
import data.Constants
import mu.KotlinLogging
import sim.Event
import sim.SimIteration
import kotlin.random.Random

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

    private fun <T> valueByLevelDiff(sim: SimIteration, table: Map<Int, T>) : T {
        val levelDiff = sim.target.level - sim.subject.level

        return when {
            levelDiff <= 0 -> {
                logger.warn("Attempted to compute a spell hit on a target more than 3 levels below")
                table[0]!!
            }
            levelDiff > 3 -> {
                logger.warn("Attempted to compute a spell hit on a target more than 3 levels above")
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

    fun spellMissChance(sim: SimIteration): Double {
        val baseMiss = valueByLevelDiff(sim, baseMissChance)
        val spellHitChance = sim.spellHitPct() / 100.0

        // Spells can never get to 100% hit kekw
        return (baseMiss - spellHitChance).coerceAtLeast(0.01)
    }

    // https://wow.gamepedia.com/Resistance
    // https://dwarfpriest.wordpress.com/2008/01/07/spell-hit-spell-penetration-and-resistances/
    fun spellResistReduction(sim: SimIteration, school: Constants.DamageType): Double {
        val targetResistance = when(school) {
            Constants.DamageType.ARCANE -> sim.targetStats.arcaneResistance
            Constants.DamageType.FIRE -> sim.targetStats.fireResistance
            Constants.DamageType.FROST -> sim.targetStats.frostResistance
            Constants.DamageType.NATURE -> sim.targetStats.natureResistance
            Constants.DamageType.SHADOW -> sim.targetStats.shadowResistance
            else -> 0
        }

        // TODO: Model partial resists as 0/25/50/75
        //       There seems to be no real formula for that, though, so just going with avg every time for now
        val totalResistance = (targetResistance - sim.subjectStats.spellPen).coerceAtLeast(0) + targetResistance
        return (0.75 * totalResistance / (5 * sim.subject.level.toDouble())).coerceAtMost(0.75).coerceAtLeast(0.00)
    }

    fun spellCritChance(sim: SimIteration): Double {
        return (sim.spellCritPct() / 100.0).coerceAtLeast(0.0)
    }

    fun baseDamageRoll(sim: SimIteration, minDmg: Double, maxDmg: Double, spellDamageCoeff: Double = 1.0, school: Constants.DamageType, bonusSpellDamage: Int = 0): Double {
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)
        val dmg = Random.nextDouble(min, max)
        return baseDamageRoll(sim, dmg, spellDamageCoeff, school, bonusSpellDamage)
    }

    fun baseDamageRoll(sim: SimIteration, dmg: Double, spellDamageCoeff: Double = 1.0, school: Constants.DamageType, bonusSpellDamage: Int = 0): Double {
        // Add school damage
        val schoolDamage = when(school) {
            Constants.DamageType.HOLY -> sim.subjectStats.holyDamage
            Constants.DamageType.FIRE -> sim.subjectStats.fireDamage
            Constants.DamageType.NATURE -> sim.subjectStats.natureDamage
            Constants.DamageType.FROST -> sim.subjectStats.frostDamage
            Constants.DamageType.SHADOW -> sim.subjectStats.shadowDamage
            Constants.DamageType.ARCANE -> sim.subjectStats.arcaneDamage
            else -> 0
        }

        val totalSpellDamage = sim.spellDamage() + bonusSpellDamage + schoolDamage
        return dmg + (totalSpellDamage * spellDamageCoeff)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sim: SimIteration, damageRoll: Double, school: Constants.DamageType, isBinary: Boolean = false) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val flatModifier = sim.subjectStats.spellDamageFlatModifier
        val critMultiplier = Stats.spellCritMultiplier + (1 - sim.subjectStats.spellDamageAddlCritMultiplier)
        val allMultiplier = sim.subjectStats.spellDamageMultiplier

        // Apply constant multipliers and finalize the damage roll
        val finalDamageRoll = (damageRoll + flatModifier) * allMultiplier

        // Get the attack result
        val missChance = spellMissChance(sim)
        val critChance = spellCritChance(sim)

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
        val resistAvgReduction = spellResistReduction(sim, school)
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
