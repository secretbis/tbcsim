package mechanics

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

    fun getBaseMiss(sim: SimIteration): Double {
        return valueByLevelDiff(sim, baseMissChance)
    }

    fun getMeleeMissChance(sim: SimIteration, isWhiteHit: Boolean): Double {
        val baseMiss = getBaseMiss(sim)
        val meleeHitChance = sim.subject.getMeleeHitPct()
        val modifiedMiss = (baseMiss - meleeHitChance).coerceAtLeast(0.0)

        return if(isWhiteHit) {
            if(sim.subject.isDualWielding()) {
                modifiedMiss + baseDualWieldMiss
            } else {
                modifiedMiss
            }
        } else modifiedMiss
    }

    fun getMeleeParryChance(sim: SimIteration): Double {
        return if(sim.opts.allowParryAndBlock) {
            valueByLevelDiff(sim, baseParryChance)
        } else {
            0.0
        }
    }

    fun getMeleeDodgeChance(sim: SimIteration): Double {
        return valueByLevelDiff(sim, baseDodgeChance)
    }

    fun getMeleeBlockChance(sim: SimIteration): Double {
        return if(sim.opts.allowParryAndBlock) {
            // TODO: Does this happen, and how much is mitigated by a mob?
            0.0
        } else {
            0.0
        }
    }

    fun getMeleeBlockReduction(sim: SimIteration): Double {
        // TODO: Does this happen, and how much is mitigated by a mob?
        return 0.0
    }

    fun getMeleeGlanceChance(sim: SimIteration): Double {
        return valueByLevelDiff(sim, baseGlancingChance)
    }

    fun getMeleeGlanceReduction(sim: SimIteration): Double {
        val defDifference: Int = (sim.target.level - sim.subject.level).coerceAtLeast(0) * 5
        val low = 1.3 - (0.05 * defDifference).coerceAtMost(0.6).coerceAtLeast(0.0)
        val high = 1.2 - (0.03 * defDifference).coerceAtMost(0.99).coerceAtLeast(0.2)

        return Random.nextDouble(low, high)
    }

    fun getMeleeCritChance(sim: SimIteration): Double {
        return (sim.subject.getMeleeCritPct() + baseCritChance - valueByLevelDiff(sim, critSuppression)).coerceAtLeast(0.0)
    }

    fun getMeleeArmorPen(sim: SimIteration): Double {
        return sim.subject.getArmorPen()
    }

    fun getMeleeArmorMitigation(sim: SimIteration): Double {
        val targetArmor = sim.target.stats.armor - getMeleeArmorPen(sim)
        return targetArmor / (targetArmor + (467.5 * sim.subject.level - 22167.5))
    }

    // Converts an attack power value into a flat damage modifier for a particular item
    fun apToDamage(sim: SimIteration, attackPower: Int, item: Item): Double {
        return (item.dps + (attackPower / 3.5)) * (item.speed / 1000)
    }

    fun baseDamageRoll(sim: SimIteration, item: Item, bonusAp: Int = 0): Double {
        val totalAp = sim.subject.stats.attackPower + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + apToDamage(sim, totalAp, item)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sim: SimIteration, damageRoll: Double, isWhiteDmg: Boolean = false) : Pair<Double, Event.Result> {
        val hitRoll = Random.nextDouble()

        // Generate a single roll table
        val missChance = getMeleeMissChance(sim, true)
        val dodgeChance = getMeleeDodgeChance(sim) + missChance
        val parryChance = getMeleeParryChance(sim) + dodgeChance
        val glanceChance = if(isWhiteDmg) {
            getMeleeGlanceChance(sim) + parryChance
        } else {
            parryChance
        }
        val blockChance = getMeleeBlockChance(sim) + glanceChance
        val critChance = getMeleeCritChance(sim) + blockChance

        // Find all our possible damage mods from buffs and so on
        val flatModifier = if(isWhiteDmg) {
            sim.subject.stats.whiteDamageMultiplier
        } else {
            sim.subject.stats.yellowDamageMultiplier
        }

        val critMultiplier = if(isWhiteDmg) {
            sim.subject.stats.whiteDamageCritMultiplier
        } else {
            sim.subject.stats.yellowDamageCritMultiplier
        }

        val allMultiplier = if(isWhiteDmg) {
            sim.subject.stats.whiteDamageMultiplier
        } else {
            sim.subject.stats.yellowDamageMultiplier
        }

        // Apply constant multipliers
        val finalDamageRoll = (damageRoll + flatModifier) * allMultiplier
        val unmitigated = when {
            hitRoll < missChance -> Pair(0.0, Event.Result.MISS)
            hitRoll < dodgeChance -> Pair(0.0, Event.Result.DODGE)
            hitRoll < parryChance -> Pair(0.0, Event.Result.PARRY)
            isWhiteDmg && hitRoll < glanceChance -> Pair(finalDamageRoll * (1 - getMeleeGlanceReduction(sim)), Event.Result.GLANCE)
            hitRoll < blockChance -> Pair(finalDamageRoll * (1 - getMeleeBlockReduction(sim)), Event.Result.BLOCK)
            hitRoll < critChance -> Pair(finalDamageRoll * critMultiplier, Event.Result.CRIT)
            else -> Pair(finalDamageRoll, Event.Result.HIT)
        }

        // Apply target armor mitigation
        return Pair(unmitigated.first * (1 - getMeleeArmorMitigation(sim)), unmitigated.second)
    }
}
