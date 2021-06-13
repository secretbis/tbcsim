package mechanics

import character.Stats
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
object Ranged {
    const val NORMALIZED_SPEED = 2800.0

    fun rngSuffix(sp: SimParticipant, item: Item): String {
        val castingAbility = sp.castingRule?.ability?.name ?: "Autoattack"
        return "$castingAbility ${item.name}"
    }

    fun isGun(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.GUN
    }

    fun isBow(item: Item): Boolean {
        return item.itemSubclass == Constants.ItemSubclass.BOW
    }

    fun rangedCritChance(sp: SimParticipant, item: Item): Double {
        val itemBonusCritPct = when {
            isGun(item) -> sp.stats.gunCritRating
            isBow(item) -> sp.stats.bowCritRating
            else -> 0.0
        } / Rating.critPerPct

        val baseRangedCritChance = sp.rangedCritPct() / 100.0 - General.valueByLevelDiff(sp, General.critSuppression)
        return ((itemBonusCritPct / 100.0) + baseRangedCritChance).coerceAtLeast(0.0)
    }

    fun baseMiss(sp: SimParticipant): Double {
        return General.valueByLevelDiff(sp, General.baseMissChance)
    }

    fun rangedMissChance(sp: SimParticipant): Double {
        val baseMiss = baseMiss(sp)
        val physicalHitChance = sp.physicalHitPct() / 100.0
        return (baseMiss - physicalHitChance).coerceAtLeast(0.0)
    }

    // Converts an attack power value into a flat damage modifier for a particular item
    @Suppress("UNUSED_PARAMETER")
    fun apToDamage(sp: SimParticipant, attackPower: Int, item: Item, isNormalized: Boolean = false): Double {
        val weaponSpeed = (if(isNormalized) { NORMALIZED_SPEED } else item.speed) / 1000.0
        return attackPower / 14 * weaponSpeed
    }

    fun baseDamageRoll(sp: SimParticipant, item: Item, bonusAp: Int = 0, isNormalized: Boolean = false): Double {
        val totalAp = sp.rangedAttackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + apToDamage(sp, totalAp, item, isNormalized)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sp: SimParticipant, _damageRoll: Double, item: Item, isWhiteDmg: Boolean = false, bonusCritChance: Double = 0.0) : Pair<Double, EventResult> {
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

        val damageRoll = (_damageRoll + flatModifier) * allMultiplier

        // Find all our possible damage mods from buffs and so on
        // old version was technically correct but only worked because the base crit multiplier is 2.0. general formula should be this
        val additionalCritMultiplier = (if(isWhiteDmg) {
            sp.stats.whiteDamageAddlCritMultiplier
        } else {
            sp.stats.yellowDamageAddlCritMultiplier
        })
        val critMultiplier = (Stats.physicalCritMultiplier - 1.0) * (additionalCritMultiplier) + 1

        // Get the attack result
        val missChance = rangedMissChance(sp)
        val blockChance = General.physicalBlockChance(sp) + missChance
        val critChance = if(isWhiteDmg) {
            rangedCritChance(sp, item) + bonusCritChance + blockChance
        } else {
            blockChance
        }

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, EventResult.MISS)
            attackRoll < blockChance -> Pair(damageRoll, EventResult.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(damageRoll * critMultiplier, EventResult.CRIT)
            else -> Pair(damageRoll, EventResult.HIT)
        }

        if(!isWhiteDmg) {
            // Two-roll yellow hit
            if(finalResult.second == EventResult.HIT || finalResult.second == EventResult.BLOCK) {
                val hitRoll2 = Random.nextDouble()
                finalResult = when {
                    hitRoll2 < (rangedCritChance(sp, item) + bonusCritChance) -> Pair(
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
