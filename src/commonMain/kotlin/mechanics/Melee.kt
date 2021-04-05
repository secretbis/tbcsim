package mechanics

import character.Stats
import data.Constants
import data.model.Item
import mu.KotlinLogging
import sim.Event
import sim.SimParticipant
import kotlin.js.JsExport
import kotlin.random.Random

@JsExport
object Melee {
    private val logger = KotlinLogging.logger {}

    // Base mitigation values based on level difference
    const val baseDualWieldMiss: Double = 0.19
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

    fun rngSuffix(sp: SimParticipant, item: Item): String {
        val handSuffix = if(isOffhand(sp, item)) { "OH" } else "MH"
        val castingAbility = sp.castingRule?.ability?.name ?: "Autoattack"
        return "$handSuffix $castingAbility ${item.name}"
    }

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
    fun expertisePctForItem(sp: SimParticipant, item: Item): Double {
        return when {
            isAxe(item) -> sp.stats.axeExpertiseRating
            isMace(item) -> sp.stats.maceExpertiseRating
            isSword(item) -> sp.stats.swordExpertiseRating
            else -> 0.0
        } / Rating.expertisePerPct
    }

    private fun <T> valueByLevelDiff(sp: SimParticipant, table: Map<Int, T>) : T {
        val levelDiff = sp.sim.target.character.level - sp.character.level

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

    fun isOffhand(sp: SimParticipant, item: Item): Boolean {
        return item === sp.character.gear.offHand
    }

    fun baseMiss(sp: SimParticipant, item: Item, isWhiteHit: Boolean): Double {
        val baseMissForLevel = valueByLevelDiff(sp, General.baseMissChance)

        // The heroic strike nonsense only eliminates the dual-wield penalty, and nothing further
        val offHandHitBonus = if(isOffhand(sp, item)) { sp.stats.offHandAddlWhiteHitPct / 100.0 } else 0.0
        val actualDWMissChance = (baseDualWieldMiss - offHandHitBonus).coerceAtLeast(0.0)

        return if(isWhiteHit && sp.isDualWielding()) {
            baseMissForLevel + actualDWMissChance
        } else baseMissForLevel
    }

    fun meleeMissChance(sp: SimParticipant, item: Item, isWhiteHit: Boolean): Double {
        val baseMiss = baseMiss(sp, item, isWhiteHit)
        val meleeHitChance = sp.physicalHitPct() / 100.0
        return (baseMiss - meleeHitChance).coerceAtLeast(0.0)
    }

    fun meleeParryChance(sp: SimParticipant, item: Item): Double {
        return if(sp.sim.opts.allowParryAndBlock) {
            (valueByLevelDiff(sp, baseParryChance) - (sp.expertisePct() / 100.0) - (expertisePctForItem(sp, item) / 100.0)).coerceAtLeast(0.0)
        } else {
            0.0
        }
    }

    fun meleeDodgeChance(sp: SimParticipant, item: Item): Double {
        return (valueByLevelDiff(sp, baseDodgeChance) - (sp.expertisePct() / 100.0) - (expertisePctForItem(sp, item) / 100.0)).coerceAtLeast(0.0)
    }

    fun meleeGlanceChance(sp: SimParticipant): Double {
        return valueByLevelDiff(sp, baseGlancingChance)
    }

    fun meleeGlanceReduction(sp: SimParticipant, item: Item): Double {
        val defDifference: Int = (sp.sim.target.character.level - sp.character.level).coerceAtLeast(0) * 5
        val low = 1.3 - (0.05 * defDifference).coerceAtMost(0.6).coerceAtLeast(0.0)
        val high = 1.2 - (0.03 * defDifference).coerceAtMost(0.99).coerceAtLeast(0.2)

        return Random.nextDouble(low, high)
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

    fun baseDamageRoll(sp: SimParticipant, item: Item, bonusAp: Int = 0, isNormalized: Boolean = false, isWhiteDmg: Boolean = false): Double {
        val totalAp = sp.attackPower() + bonusAp
        val min = item.minDmg.coerceAtLeast(0.0)
        val max = item.maxDmg.coerceAtLeast(1.0)

        val offHandMultiplier = if(isOffhand(sp, item)) {
            Stats.offHandPenalty + if(isWhiteDmg) {
                sp.stats.whiteDamageAddlOffHandPenaltyModifier
            } else {
                sp.stats.yellowDamageAddlOffHandPenaltyModifier
            }
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

        return (Random.nextDouble(min, max) + apToDamage(sp, totalAp, item, isNormalized) + flatModifier) * offHandMultiplier * allMultiplier
    }

    // Performs an attack roll given an initial unmitigated damage value
    fun attackRoll(sp: SimParticipant, damageRoll: Double, item: Item, isWhiteDmg: Boolean = false) : Pair<Double, Event.Result> {
        // Find all our possible damage mods from buffs and so on
        val critMultiplier = Stats.physicalCritMultiplier + (if(isWhiteDmg) {
            sp.stats.whiteDamageAddlCritMultiplier
        } else {
            sp.stats.yellowDamageAddlCritMultiplier
        } - 1)

        // Get the attack result
        val missChance = meleeMissChance(sp, item, isWhiteDmg)
        val dodgeChance = meleeDodgeChance(sp, item) + missChance
        val parryChance = meleeParryChance(sp, item) + dodgeChance
        val glanceChance = if(isWhiteDmg) {
            meleeGlanceChance(sp) + parryChance
        } else {
            parryChance
        }
        val blockChance = General.physicalBlockChance(sp) + glanceChance
        val critChance = if(isWhiteDmg) {
            General.physicalCritChance(sp) + blockChance
        } else {
            blockChance
        }

        val attackRoll = Random.nextDouble()
        var finalResult = when {
            attackRoll < missChance -> Pair(0.0, Event.Result.MISS)
            attackRoll < dodgeChance -> Pair(0.0, Event.Result.DODGE)
            attackRoll < parryChance -> Pair(0.0, Event.Result.PARRY)
            isWhiteDmg && attackRoll < glanceChance -> Pair(damageRoll * (1 - meleeGlanceReduction(sp, item)), Event.Result.GLANCE)
            attackRoll < blockChance -> Pair(damageRoll, Event.Result.BLOCK) // Blocked damage is reduced later
            isWhiteDmg && attackRoll < critChance -> Pair(damageRoll * critMultiplier, Event.Result.CRIT)
            else -> Pair(damageRoll, Event.Result.HIT)
        }

        if(!isWhiteDmg) {
            // Two-roll yellow hit
            if(finalResult.second == Event.Result.HIT || finalResult.second == Event.Result.BLOCK) {
                val hitRoll2 = Random.nextDouble()
                finalResult = when {
                    hitRoll2 < General.physicalCritChance(sp) -> Pair(
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
