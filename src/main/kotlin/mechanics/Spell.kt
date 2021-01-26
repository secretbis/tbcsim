package mechanics

import character.Stats
import data.Constants
import mu.KotlinLogging
import sim.Event
import sim.SimIteration
import kotlin.random.Random

object Spell {
    private val logger = KotlinLogging.logger {}

    const val baseCritChance: Double = 0.05

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

    fun spellMissChance(sim: SimIteration): Double {
        val baseMiss = valueByLevelDiff(sim, baseMissChance)
        val spellHitChance = sim.subject.spellHitPct() / 100.0

        // Spells can never get to 100% hit kekw
        return (baseMiss - spellHitChance).coerceAtLeast(0.01)
    }

    // https://wow.gamepedia.com/Resistance
    // https://dwarfpriest.wordpress.com/2008/01/07/spell-hit-spell-penetration-and-resistances/
    fun spellResistReduction(sim: SimIteration, school: Constants.DamageType): Double {
        val targetResistance = when(school) {
            Constants.DamageType.ARCANE -> sim.target.stats.arcaneResistance
            Constants.DamageType.FIRE -> sim.target.stats.fireResistance
            Constants.DamageType.FROST -> sim.target.stats.frostResistance
            Constants.DamageType.NATURE -> sim.target.stats.natureResistance
            Constants.DamageType.SHADOW -> sim.target.stats.shadowResistance
            else -> 0
        }

        // TODO: Model partial resists as 0/25/50/75
        //       There seems to be no real formula for that, though, so just going with avg every time for now
        val totalResistance = (targetResistance - sim.subject.stats.spellPen).coerceAtLeast(0) + targetResistance
        return (0.75 * targetResistance / (5 * sim.subject.level.toDouble())).coerceAtMost(0.75).coerceAtLeast(0.00)
    }

    fun spellCritChance(sim: SimIteration): Double {
        return (sim.subject.spellCritPct() / 100.0 + baseCritChance).coerceAtLeast(0.0)
    }

    fun baseDamageRoll(sim: SimIteration, minDmg: Double, maxDmg: Double, spellDamageCoeff: Double = 1.0, school: Constants.DamageType, bonusSpellDamage: Int = 0, ): Double {
        val totalSpellDamage = sim.subject.spellDamage() + bonusSpellDamage
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + (totalSpellDamage * spellDamageCoeff)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sim: SimIteration, damageRoll: Double, school: Constants.DamageType, isBinary: Boolean = false) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val flatModifier = sim.subject.stats.spellDamageFlatModifier
        val critMultiplier = Stats.spellCritMultiplier + (1 - sim.subject.stats.spellDamageAddlCritMultiplier)
        val allMultiplier = sim.subject.stats.spellDamageMultiplier

        // Apply constant multipliers and finalize the damage roll
        val finalDamageRoll = (damageRoll + flatModifier) * allMultiplier

        // Get the attack result
        val missChance = spellMissChance(sim)
        val critChance = spellCritChance(sim)

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, Event.Result.MISS)
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
