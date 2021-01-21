package mechanics

import character.Stats
import data.model.Item
import mu.KotlinLogging
import sim.Event
import sim.SimIteration
import kotlin.random.Random

object Melee {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    const val baseDualWieldMiss: Double = 0.19
    const val baseCritChance: Double = 0.05
    val baseMissChance = mapOf(
        0 to 0.05,
        1 to 0.055,
        2 to 0.06,
        3 to 0.09
    )
    // TODO: Wasn't able to find confirmed parry values for 71/72 mobs
    //       Assumes 14% boss parry persists to TBC
    val baseParryChance = mapOf(
        0 to 0.05,
        1 to 0.055,
        2 to 0.06,
        3 to 0.14
    )
    val baseDodgeChance = mapOf(
        0 to 0.05,
        1 to 0.055,
        2 to 0.06,
        3 to 0.065
    )
    // TODO: Does TBC still have this?
    val critSuppression = mapOf(
        0 to 0.00,
        1 to 0.01,
        2 to 0.02,
        3 to 0.03
    )
    val baseGlancingChance = mapOf(
        0 to 0.10,
        1 to 0.15,
        2 to 0.20,
        3 to 0.25
    )

    private fun <T> valueByLevelDiff(sim: SimIteration, table: Map<Int, T>) : T {
        val levelDiff = sim.target.level - sim.subject.level

        return when {
            levelDiff <= 0 -> {
                logger.warn("Attempted to compute a melee hit on a target more than 3 levels below")
                table[0]!!
            }
            levelDiff > 3 -> {
                logger.warn("Attempted to compute a melee hit on a target more than 3 levels above")
                table[3]!!
            }
            else -> table[levelDiff]!!
        }
    }

    fun baseMiss(sim: SimIteration, isWhiteHit: Boolean): Double {
        val baseMissForLevel = valueByLevelDiff(sim, baseMissChance)
        return if(isWhiteHit && sim.subject.isDualWielding()) {
            baseMissForLevel + baseDualWieldMiss
        } else baseMissForLevel
    }

    fun meleeMissChance(sim: SimIteration, isWhiteHit: Boolean): Double {
        val baseMiss = baseMiss(sim, isWhiteHit)
        val meleeHitChance = sim.subject.meleeHitPct() / 100.0
        return (baseMiss - meleeHitChance).coerceAtLeast(0.0)
    }

    fun meleeParryChance(sim: SimIteration): Double {
        return if(sim.opts.allowParryAndBlock) {
            valueByLevelDiff(sim, baseParryChance)
        } else {
            0.0
        }
    }

    fun meleeDodgeChance(sim: SimIteration): Double {
        return valueByLevelDiff(sim, baseDodgeChance)
    }

    fun meleeBlockChance(sim: SimIteration): Double {
        return if(sim.opts.allowParryAndBlock) {
            // Mobs cannot block more than 5% of the time
            // https://github.com/magey/classic-warrior/wiki/Attack-table#block
            0.05
        } else {
            0.0
        }
    }

    fun meleeBlockReduction(sim: SimIteration): Double {
        // TODO: How much is mitigated by a mob?
        // This is 46 for now since that's what Thaddius blocks for
        return 46.0
    }

    fun meleeGlanceChance(sim: SimIteration): Double {
        return valueByLevelDiff(sim, baseGlancingChance)
    }

    fun meleeGlanceReduction(sim: SimIteration): Double {
        val defDifference: Int = (sim.target.level - sim.subject.level).coerceAtLeast(0) * 5
        val low = 1.3 - (0.05 * defDifference).coerceAtMost(0.6).coerceAtLeast(0.0)
        val high = 1.2 - (0.03 * defDifference).coerceAtMost(0.99).coerceAtLeast(0.2)

        return Random.nextDouble(low, high)
    }

    fun meleeCritChance(sim: SimIteration): Double {
        return (sim.subject.meleeCritPct() + baseCritChance - valueByLevelDiff(sim, critSuppression)).coerceAtLeast(0.0)
    }

    fun meleeArmorPen(sim: SimIteration): Int {
        return sim.subject.armorPen()
    }

    fun meleeArmorMitigation(sim: SimIteration): Double {
        val targetArmor = sim.target.armor() - meleeArmorPen(sim)
        return targetArmor / (targetArmor + (467.5 * sim.subject.level - 22167.5))
    }

    // Converts an attack power value into a flat damage modifier for a particular item
    fun apToDamage(sim: SimIteration, attackPower: Int, item: Item): Double {
        return (item.dps + (attackPower / 3.5)) * (item.speed / 1000)
    }

    fun baseDamageRoll(sim: SimIteration, item: Item, bonusAp: Int = 0): Double {
        val totalAp = sim.subject.attackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + apToDamage(sim, totalAp, item)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sim: SimIteration, damageRoll: Double, isWhiteDmg: Boolean = false) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val flatModifier = if(isWhiteDmg) {
            sim.subject.stats.yellowDamageFlatModifier
        } else {
            sim.subject.stats.yellowDamageFlatModifier
        }

        val critMultiplier = Stats.physicalCritMultiplier + (1 - if(isWhiteDmg) {
            sim.subject.stats.whiteDamageAddlCritMultiplier
        } else {
            sim.subject.stats.yellowDamageAddlCritMultiplier
        })

        val allMultiplier = if(isWhiteDmg) {
            sim.subject.stats.whiteDamageMultiplier
        } else {
            sim.subject.stats.yellowDamageMultiplier
        }

        // Apply constant multipliers and finalize the damage roll
        val finalDamageRoll = (damageRoll + flatModifier) * allMultiplier

        // Get the attack result
        val missChance = meleeMissChance(sim, true)
        val dodgeChance = meleeDodgeChance(sim) + missChance
        val parryChance = meleeParryChance(sim) + dodgeChance
        val glanceChance = if(isWhiteDmg) {
            meleeGlanceChance(sim) + parryChance
        } else {
            parryChance
        }
        val blockChance = meleeBlockChance(sim) + glanceChance
        val critChance = if(isWhiteDmg) {
            meleeCritChance(sim) + blockChance
        } else {
            blockChance
        }

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, Event.Result.MISS)
            attackRoll < dodgeChance -> Pair(0.0, Event.Result.DODGE)
            attackRoll < parryChance -> Pair(0.0, Event.Result.PARRY)
            isWhiteDmg && attackRoll < glanceChance -> Pair(finalDamageRoll * (1 - meleeGlanceReduction(sim)), Event.Result.GLANCE)
            attackRoll < blockChance -> Pair(finalDamageRoll, Event.Result.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(finalDamageRoll * critMultiplier, Event.Result.CRIT)
            else -> Pair(finalDamageRoll, Event.Result.HIT)
        }

        if(!isWhiteDmg) {
            // Two-roll yellow hit
            if(finalResult.second == Event.Result.HIT || finalResult.second == Event.Result.BLOCK) {
                val hitRoll2 = Random.nextDouble()
                finalResult = when {
                    hitRoll2 < meleeCritChance(sim) -> Pair(
                        finalResult.first * critMultiplier,
                        Event.Result.CRIT
                    )
                    else -> finalResult
                }
            }
        }

        // Apply target armor mitigation
        finalResult = Pair(finalResult.first * (1 - meleeArmorMitigation(sim)), finalResult.second)

        // If the attack was blocked, reduce by the block value
        if(finalResult.second == Event.Result.BLOCK || finalResult.second == Event.Result.BLOCKED_CRIT) {
            finalResult = Pair(finalResult.first - meleeBlockReduction(sim), finalResult.second)
        }

        return finalResult
    }
}
