package mechanics

import character.Stats
import data.Constants
import data.model.Item
import sim.Event
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
object Ranged {
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
    fun apToDamage(sp: SimParticipant, attackPower: Int, item: Item): Double {
        return attackPower / 14 * (item.speed / 1000.0)
    }

    fun baseDamageRoll(sp: SimParticipant, item: Item, bonusAp: Int = 0, isWhiteDmg: Boolean = false): Double {
        val totalAp = sp.rangedAttackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        return Random.nextDouble(min, max) + apToDamage(sp, totalAp, item)
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sp: SimParticipant, _damageRoll: Double, item: Item, isWhiteDmg: Boolean = false, bonusCritChance: Double = 0.0) : Pair<Double, Event.Result> {
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
        val critMultiplier = Stats.physicalCritMultiplier + (if(isWhiteDmg) {
            sp.stats.whiteDamageAddlCritMultiplier
        } else {
            sp.stats.yellowDamageAddlCritMultiplier
        } - 1)

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
            attackRoll < missChance -> Pair(0.0, Event.Result.MISS)
            attackRoll < blockChance -> Pair(damageRoll, Event.Result.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(damageRoll * critMultiplier, Event.Result.CRIT)
            else -> Pair(damageRoll, Event.Result.HIT)
        }

        if(!isWhiteDmg) {
            // Two-roll yellow hit
            if(finalResult.second == Event.Result.HIT || finalResult.second == Event.Result.BLOCK) {
                val hitRoll2 = Random.nextDouble()
                finalResult = when {
                    hitRoll2 < (rangedCritChance(sp, item) + bonusCritChance) -> Pair(
                        finalResult.first * critMultiplier,
                        Event.Result.CRIT
                    )
                    else -> finalResult
                }
            }
        }

        // Apply target armor mitigation
        finalResult = Pair(finalResult.first * (1 - General.physicalArmorMitigation(sp)), finalResult.second)

        // If the attack was blocked, reduce by the block value
        if(finalResult.second == Event.Result.BLOCK || finalResult.second == Event.Result.BLOCKED_CRIT) {
            finalResult = Pair(finalResult.first - General.physicalBlockReduction(sp), finalResult.second)
        }

        return finalResult
    }
}
