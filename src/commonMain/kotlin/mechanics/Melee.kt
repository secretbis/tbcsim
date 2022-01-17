package mechanics

import character.Stats
import character.classes.boss.Boss
import data.Constants
import data.model.Item
import mu.KotlinLogging
import sim.EventResult
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
object Melee {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    private const val baseMissChance: Double = 0.05
    private const val baseDualWieldMissChance: Double = 0.19
    private const val baseParryChance = 0.05
    private const val baseDodgeChance = 0.05

    // Instant yellow attack AP normalization
    private val normalizedWeaponSpeedMs: Map<Constants.ItemSubclass, Double> = mapOf(
        Constants.ItemSubclass.AXE_2H to 3300.0,
        Constants.ItemSubclass.MACE_2H to 3300.0,
        Constants.ItemSubclass.SWORD_2H to 3300.0,
        Constants.ItemSubclass.POLEARM to 3300.0,
        Constants.ItemSubclass.DAGGER to 1700.0,
        Constants.ItemSubclass.AXE_1H to 2400.0,
        Constants.ItemSubclass.MACE_1H to 2400.0,
        Constants.ItemSubclass.SWORD_1H to 2400.0,
        Constants.ItemSubclass.FIST to 2400.0,
        // TODO: Druid weirdness
    )

    // Computes additional item-specific expertise, i.e. racial abilities
    fun expertisePctForItem(sp: SimParticipant, item: Item?): Double {
        if(item == null) return 0.0
        return when {
            item.isAxe() -> sp.stats.axeExpertiseRating
            item.isMace() -> sp.stats.maceExpertiseRating
            item.isSword() -> sp.stats.swordExpertiseRating
            else -> 0.0
        } / Rating.expertisePerPct
    }

    fun isOffhand(sp: SimParticipant, item: Item?): Boolean {
        if(item == null) return false
        return item === sp.character.gear.offHand
    }

    fun meleeMissChance(sp: SimParticipant, item: Item?, isWhiteHit: Boolean): Double {
        val baseMiss = General.baseMiss(sp)

        // The heroic strike nonsense only eliminates the dual-wield penalty, and nothing further
        val offHandHitBonus = if (isOffhand(sp, item)) {
            sp.stats.offHandAddlWhiteHitPct / 100.0
        } else 0.0
        val actualDWMissChance = (baseDualWieldMissChance - offHandHitBonus).coerceAtLeast(0.0)

        val totalMiss = if (isWhiteHit && sp.isDualWielding()) {
            baseMiss + actualDWMissChance
        } else baseMiss

        val meleeHitChance = sp.physicalHitPct() / 100.0
        return (totalMiss - meleeHitChance).coerceAtLeast(0.0)
    }

    fun meleeParryChance(sp: SimParticipant, item: Item?): Double {
        return if(sp.sim.opts.allowParryAndBlock) {
            val baseParry = if(sp.target().isBoss()) {
                val levelDiff = General.levelDiff(sp)
                if(levelDiff > 2) {
                    baseParryChance + (levelDiff * 0.03)
                } else {
                    baseParryChance + (levelDiff * 0.005)
                }
            } else {
                sp.target().parryPct() / 100 + General.defenseChance(sp)
            }

            val expertiseReduction = (sp.expertisePct() + expertisePctForItem(sp, item)) / 100
            return (baseParry - expertiseReduction).coerceAtLeast(0.0)
        } else {
            0.0
        }
    }

    fun meleeDodgeChance(sp: SimParticipant, item: Item?): Double {
        val baseDodge = if(sp.target().isBoss()) {
            val levelDiff = General.levelDiff(sp)
            baseDodgeChance + (levelDiff * 0.005)
        } else {
            sp.target().dodgePct() / 100 + General.defenseChance(sp)
        }

        val expertiseReduction = (sp.expertisePct() + expertisePctForItem(sp, item)) / 100
        return (baseDodge - expertiseReduction).coerceAtLeast(0.0)
    }

    fun meleeGlanceChance(sp: SimParticipant): Double {
        val levelDiff = General.levelDiff(sp)
        return (0.06 + (levelDiff * 0.06)).coerceAtLeast(0.0)
    }

    fun meleeGlanceMultiplier(sp: SimParticipant, item: Item?): Double {
        val defDifference: Int = (sp.target().character.level - sp.character.level).coerceAtLeast(0) * 5
        val low = 1.3 - (0.05 * defDifference).coerceAtMost(0.91).coerceAtLeast(0.01)
        val high = 1.2 - (0.03 * defDifference).coerceAtMost(0.99).coerceAtLeast(0.2)

        return Random.nextDouble(low, high)
    }

    fun meleeCrushChance(sp: SimParticipant): Double {
        val baseCrush = if(sp.isBoss()) {
            // Crushes only occur with a mob hitting a lower-level player target
            val levelDiff = General.levelDiff(sp)
            if(levelDiff < 0) {
                (levelDiff * -0.1) - 0.15
            } else 0.0
        } else 0.0

        return baseCrush.coerceAtLeast(0.0)
    }

    // Converts an attack power value into a flat damage modifier for a particular item
    @Suppress("UNUSED_PARAMETER")
    fun apToDamage(sp: SimParticipant, attackPower: Int, item: Item, isNormalized: Boolean = false): Double {
        val speed = if(isNormalized) {
            normalizedWeaponSpeedMs[item.itemSubclass]
                ?: throw Exception("Weapon subClass has no normalization coefficient: ${item.itemSubclass}")
        } else item.speed

        return attackPower / 14 * (speed / 1000.0)
    }

    fun baseDamageRoll(sp: SimParticipant, item: Item, bonusAp: Int = 0, isNormalized: Boolean = false): Double {
        val totalAp = sp.attackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + apToDamage(sp, totalAp, item, isNormalized)
    }

    fun baseDamageRollPure(minDmg: Double, maxDmg: Double): Double {
        val min = minDmg.coerceAtLeast(0.0)
        val max = maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max)
    }

    fun additionalWeaponTypeCritChance(sp: SimParticipant, item: Item?): Double {
        return when(item?.itemSubclass) {
            Constants.ItemSubclass.DAGGER -> sp.stats.daggerAdditionalCritChancePercent
            Constants.ItemSubclass.FIST -> sp.stats.fistWeaponAdditionalCritChancePercent
            else -> 0.0
        }
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sp: SimParticipant, _damageRoll: Double, item: Item?, isWhiteDmg: Boolean = false, abilityAdditionalCritDamageMultiplier: Double = 1.0, bonusCritChance: Double = 0.0, noDodgeAllowed: Boolean = false) : Pair<Double, EventResult> {
        val offHandMultiplier = if(isOffhand(sp, item)) {
            Stats.offHandPenalty * (if(isWhiteDmg) {
                sp.stats.whiteDamageAddlOffHandPenaltyModifier
            } else {
                sp.stats.yellowDamageAddlOffHandPenaltyModifier
            } + 1)
        } else {
            1.0
        }

        val flatModifier = if(isWhiteDmg) {
            sp.stats.whiteDamageFlatModifier
        } else {
            sp.stats.yellowDamageFlatModifier
        }

        val allMultiplier = if(isWhiteDmg) {
            sp.stats.whiteDamageMultiplier
        } else {
            sp.stats.yellowDamageMultiplier
        } * sp.stats.physicalDamageMultiplier

        val damageRoll = (_damageRoll + flatModifier) * offHandMultiplier * allMultiplier

        // Find all our possible damage mods from buffs and so on
        // old version was technically correct but only worked because the base crit multiplier is 2.0. general formula should be this
        val additionalCritMultiplier = (if(isWhiteDmg) {
            sp.stats.whiteDamageAddlCritMultiplier
        } else {
            sp.stats.yellowDamageAddlCritMultiplier
        })
        val critMultiplier = (Stats.physicalCritMultiplier - 1.0) * (additionalCritMultiplier * abilityAdditionalCritDamageMultiplier) + 1

        // Get the attack result
        val missChance = meleeMissChance(sp, item, isWhiteDmg)
        val actualCritChance = General.baseCrit(sp, item) + bonusCritChance + additionalWeaponTypeCritChance(sp, item) + sp.stats.yellowHitsAdditionalCritPct
        val dodgeChance = if(noDodgeAllowed) 0.0 else meleeDodgeChance(sp, item) + missChance
        val parryChance = meleeParryChance(sp, item) + dodgeChance
        val glanceChance = if(isWhiteDmg) {
            meleeGlanceChance(sp) + parryChance
        } else {
            parryChance
        }
        val blockChance = General.physicalBlockChance(sp) + glanceChance
        val critChance = if(isWhiteDmg) {
            actualCritChance + blockChance
        } else {
            blockChance
        }
        val crushChance = meleeCrushChance(sp) + critChance

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, EventResult.MISS)
            attackRoll < dodgeChance -> Pair(0.0, EventResult.DODGE)
            attackRoll < parryChance -> Pair(0.0, EventResult.PARRY)
            isWhiteDmg && attackRoll < glanceChance -> Pair(damageRoll * meleeGlanceMultiplier(sp, item), EventResult.GLANCE)
            attackRoll < blockChance -> Pair(damageRoll, EventResult.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(damageRoll * critMultiplier, EventResult.CRIT)
            isWhiteDmg && attackRoll < crushChance -> Pair(damageRoll * critMultiplier, EventResult.CRUSH)
            else -> Pair(damageRoll, EventResult.HIT)
        }

        if(!isWhiteDmg) {
            // Two-roll yellow hit
            if(finalResult.second == EventResult.HIT || finalResult.second == EventResult.BLOCK) {
                val hitRoll2 = Random.nextDouble()
                finalResult = when {
                    hitRoll2 < actualCritChance -> Pair(
                        finalResult.first * critMultiplier,
                        EventResult.CRIT
                    )
                    else -> finalResult
                }
            }
        }

        // Apply target armor mitigation
        finalResult = Pair(finalResult.first * (1 - General.physicalArmorMitigation(sp)), finalResult.second)

        // If the attack was blocked, reduce by the block value
        if(finalResult.second == EventResult.BLOCK || finalResult.second == EventResult.BLOCKED_CRIT) {
            finalResult = Pair(finalResult.first - General.physicalBlockReduction(sp), finalResult.second)
        }

        return finalResult
    }
}
