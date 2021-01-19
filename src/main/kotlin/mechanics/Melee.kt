package mechanics

import mu.KotlinLogging
import sim.Sim
import kotlin.random.Random

object Melee {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    val baseDualWieldMiss: Double = 0.19
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

    private fun <T> valueByLevelDiff(sim: Sim, table: Map<Int, T>) : T {
        val levelDiff = sim.opts.targetLevel - sim.subject.level

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

    fun getBaseMiss(sim: Sim): Double {
        return valueByLevelDiff(sim, baseMissChance)
    }

    fun getMeleeMissChance(sim: Sim, isWhiteHit: Boolean): Double {
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

    fun getMeleeParryChance(sim: Sim): Double {
        return if(sim.opts.allowParryAndBlock) {
            valueByLevelDiff(sim, baseParryChance)
        } else {
            0.0
        }
    }

    fun getMeleeDodgeChance(sim: Sim): Double {
        return valueByLevelDiff(sim, baseDodgeChance)
    }

    fun getMeleeBlockChance(sim: Sim): Double {
        return if(sim.opts.allowParryAndBlock) {
            // TODO: Does this happen, and how much is mitigated by a mob?
            0.0
        } else {
            0.0
        }
    }

    fun getMeleeBlockReduction(sim: Sim): Double {
        // TODO: Does this happen, and how much is mitigated by a mob?
        return 0.0
    }

    fun getMeleeGlanceChance(sim: Sim): Double {
        return valueByLevelDiff(sim, baseGlancingChance)
    }

    fun getMeleeGlanceReduction(sim: Sim): Double {
        val defDifference: Int = (sim.target.level - sim.subject.level).coerceAtLeast(0) * 5
        val low = 1.3 - (0.05 * defDifference).coerceAtMost(0.6).coerceAtLeast(0.0)
        val high = 1.2 - (0.03 * defDifference).coerceAtMost(0.99).coerceAtLeast(0.2)

        return Random.nextDouble(low, high)
    }

    fun getMeleeCritChance(sim: Sim): Double {
        return sim.subject.getMeleeCritPct()
    }

    fun getMeleeArmorPen(sim: Sim): Double {
        return sim.subject.getArmorPen()
    }

    fun getMeleeArmorMitigation(sim: Sim): Double {
        val targetArmor = sim.opts.targetArmor - getMeleeArmorPen(sim)
        return targetArmor / (targetArmor + (467.5 * sim.subject.level - 22167.5))
    }
}
