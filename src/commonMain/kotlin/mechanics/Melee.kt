package mechanics

import character.Stats
import data.Constants
import data.model.Item
import mu.KotlinLogging
import sim.Event
import sim.SimIteration
import kotlin.random.Random

object Melee {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    const val baseDualWieldMiss: Double = 0.19
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

    // Instant yellow attack AP normalization
    val normalizedWeaponSpeedMs: Map<Constants.ItemSubclass, Double> = mapOf(
        Constants.ItemSubclass.AXE_2H to 3300.0,
        Constants.ItemSubclass.MACE_2H to 3300.0,
        Constants.ItemSubclass.SWORD_2H to 3300.0,
        Constants.ItemSubclass.DAGGER to 1700.0,
        Constants.ItemSubclass.AXE_1H to 2400.0,
        Constants.ItemSubclass.MACE_1H to 2400.0,
        Constants.ItemSubclass.SWORD_1H to 2400.0,
        Constants.ItemSubclass.FIST to 2400.0,
        // TODO: Druid weirdness
    )

    fun is2H(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.SWORD_2H ||
               item.itemSubclass == Constants.ItemSubclass.AXE_2H ||
               item.itemSubclass == Constants.ItemSubclass.MACE_2H ||
               item.itemSubclass == Constants.ItemSubclass.POLEARM ||
               item.itemSubclass == Constants.ItemSubclass.STAFF
    }

    fun isAxe(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.AXE_2H ||
               item.itemSubclass == Constants.ItemSubclass.AXE_1H
    }

    fun isMace(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.MACE_2H ||
               item.itemSubclass == Constants.ItemSubclass.MACE_1H
    }

    fun isPoleaxe(item: Item): Boolean {
        return isAxe(item) || item.itemSubclass == Constants.ItemSubclass.POLEARM
    }

    fun isSword(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.SWORD_2H ||
               item.itemSubclass == Constants.ItemSubclass.SWORD_1H
    }

    // Computes additional item-specific expertise, i.e. racial abilities
    fun expertisePctForItem(sim: SimIteration, item: Item): Double {
        return when {
            isAxe(item) -> sim.subjectStats.axeExpertiseRating
            isMace(item) -> sim.subjectStats.maceExpertiseRating
            isSword(item) -> sim.subjectStats.swordExpertiseRating
            else -> 0.0
        } / Rating.expertisePerPct
    }

    private fun <T> valueByLevelDiff(sim: SimIteration, table: Map<Int, T>) : T {
        val levelDiff = sim.target.level - sim.subject.level

        return when {
            levelDiff <= 0 -> {
                logger.warn { "Attempted to compute a melee hit on a target more than 3 levels below" }
                table[0]!!
            }
            levelDiff > 3 -> {
                logger.warn { "Attempted to compute a melee hit on a target more than 3 levels above" }
                table[3]!!
            }
            else -> table[levelDiff]!!
        }
    }

    fun isOffhand(sim: SimIteration, item: Item): Boolean {
        return item === sim.subject.gear.offHand
    }

    fun baseMiss(sim: SimIteration, item: Item, isWhiteHit: Boolean): Double {
        val baseMissForLevel = valueByLevelDiff(sim, baseMissChance)

        // The heroic strike nonsense only eliminates the dual-wield penalty, and nothing further
        val offHandHitBonus = if(isOffhand(sim, item)) { sim.subjectStats.offHandAddlWhiteHitPct / 100.0 } else 0.0
        val actualDWMissChance = (baseDualWieldMiss - offHandHitBonus).coerceAtLeast(0.0)

        return if(isWhiteHit && sim.isDualWielding()) {
            baseMissForLevel + actualDWMissChance
        } else baseMissForLevel
    }

    fun meleeMissChance(sim: SimIteration, item: Item, isWhiteHit: Boolean): Double {
        val baseMiss = baseMiss(sim, item, isWhiteHit)
        val meleeHitChance = sim.meleeHitPct() / 100.0
        return (baseMiss - meleeHitChance).coerceAtLeast(0.0)
    }

    fun meleeParryChance(sim: SimIteration, item: Item): Double {
        return if(sim.opts.allowParryAndBlock) {
            (valueByLevelDiff(sim, baseParryChance) - (sim.expertisePct() / 100.0) - (expertisePctForItem(sim, item) / 100.0)).coerceAtLeast(0.0)
        } else {
            0.0
        }
    }

    fun meleeDodgeChance(sim: SimIteration, item: Item): Double {
        return (valueByLevelDiff(sim, baseDodgeChance) - (sim.expertisePct() / 100.0) - (expertisePctForItem(sim, item) / 100.0)).coerceAtLeast(0.0)
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

    @Suppress("UNUSED_PARAMETER")
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
        return (sim.meleeCritPct() / 100.0 - valueByLevelDiff(sim, critSuppression)).coerceAtLeast(0.0)
    }

    fun meleeArmorPen(sim: SimIteration): Int {
        return sim.armorPen()
    }

    fun meleeArmorMitigation(sim: SimIteration): Double {
        val targetArmor = (sim.targetArmor() - meleeArmorPen(sim)).coerceAtLeast(0)
        return targetArmor / (targetArmor + (467.5 * sim.target.level - 22167.5))
    }

    // Converts an attack power value into a flat damage modifier for a particular item
    @Suppress("UNUSED_PARAMETER")
    fun apToDamage(sim: SimIteration, attackPower: Int, item: Item, isNormalized: Boolean = false): Double {
        val speed = if(isNormalized) {
            normalizedWeaponSpeedMs[item.itemSubclass]
                ?: throw Exception("Weapon subClass has no normalization coefficient: ${item.itemSubclass}")
        } else item.speed

        return attackPower / 14 * (speed / 1000.0)
    }

    fun baseDamageRoll(sim: SimIteration, item: Item, bonusAp: Int = 0, isNormalized: Boolean = false, isWhiteDmg: Boolean = false): Double {
        val totalAp = sim.attackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        val offHandMultiplier = if(isOffhand(sim, item)) {
            Stats.offHandPenalty + if(isWhiteDmg) {
                sim.subjectStats.whiteDamageAddlOffHandPenaltyModifier
            } else {
                sim.subjectStats.yellowDamageAddlOffHandPenaltyModifier
            }
        } else {
            1.0
        }

        val flatModifier = if(isWhiteDmg) {
            sim.subjectStats.whiteDamageFlatModifier
        } else {
            sim.subjectStats.yellowDamageFlatModifier
        }

        val allMultiplier = if(isWhiteDmg) {
            sim.subjectStats.whiteDamageMultiplier
        } else {
            sim.subjectStats.yellowDamageMultiplier
        } * sim.subjectStats.physicalDamageMultiplier

        return (Random.nextDouble(min, max) + apToDamage(sim, totalAp, item, isNormalized) + flatModifier) * offHandMultiplier * allMultiplier
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sim: SimIteration, damageRoll: Double, item: Item, isWhiteDmg: Boolean = false) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val critMultiplier = Stats.physicalCritMultiplier + (if(isWhiteDmg) {
            sim.subjectStats.whiteDamageAddlCritMultiplier
        } else {
            sim.subjectStats.yellowDamageAddlCritMultiplier
        } - 1)

        // Get the attack result
        val missChance = meleeMissChance(sim, item, isWhiteDmg)
        val dodgeChance = meleeDodgeChance(sim, item) + missChance
        val parryChance = meleeParryChance(sim, item) + dodgeChance
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
            isWhiteDmg && attackRoll < glanceChance -> Pair(damageRoll * (1 - meleeGlanceReduction(sim)), Event.Result.GLANCE)
            attackRoll < blockChance -> Pair(damageRoll, Event.Result.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(damageRoll * critMultiplier, Event.Result.CRIT)
            else -> Pair(damageRoll, Event.Result.HIT)
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
//        finalResult = Pair(finalResult.first * (1 - meleeArmorMitigation(sim)), finalResult.second)

        // If the attack was blocked, reduce by the block value
        if(finalResult.second == Event.Result.BLOCK || finalResult.second == Event.Result.BLOCKED_CRIT) {
            finalResult = Pair(finalResult.first - meleeBlockReduction(sim), finalResult.second)
        }

        return finalResult
    }
}
