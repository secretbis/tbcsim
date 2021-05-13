package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import mechanics.Rating
import mechanics.Spell
import data.itemsets.AssassinationArmor
import data.itemsets.Deathmantle

class Eviscerate : FinisherAbility() {
    companion object {
        const val name = "Eviscerate"
    }

    override val id: Int = 26865
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double {
        val assArmor = sp.buffs[AssassinationArmor.FOUR_SET_BUFF_NAME]
        var reduction = if (assArmor != null) { AssassinationArmor.fourSetEnergyReduction() } else 0.0

        return 35.0 - reduction
    }

    override fun cast(sp: SimParticipant) {
        var increasedDamagePercent = 0.0

        val improved = sp.character.klass.talents[ImprovedEviscerate.name] as ImprovedEviscerate?
        increasedDamagePercent += improved?.damageIncreasePercent() ?: 0.0

        val aggression = sp.character.klass.talents[Aggression.name] as Aggression?
        increasedDamagePercent += aggression?.damageIncreasePercent() ?: 0.0

        val multiplier = 1 + (increasedDamagePercent / 100.0).coerceAtLeast(0.0)

        val damage = damage(sp, consumedComboPoints, multiplier)
        val damageRoll = Melee.baseDamageRollPure(damage.first, damage.second)
        val result = Melee.attackRoll(sp, damageRoll, null, isWhiteDmg = false, noDodgeAllowed = noDodgeAllowed(sp))

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            comboPointsSpent = consumedComboPoints,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        fireProcAsFinisher(sp, triggerTypes, null, event)
    }

    fun damage(sp: SimParticipant, cps: Int, dmgMultiplier: Double): Pair<Double, Double> {

        val deathmantle = sp.buffs[Deathmantle.TWO_SET_BUFF_NAME]
        val bonusDamagePerCP = if (deathmantle != null) { Deathmantle.twoSetBonusDamagePerCP() } else 0.0

        val modifier = sp.attackPower() * (cps * 0.03)
        return when(consumedComboPoints){
            1 -> Pair(60*dmgMultiplier + 185 + modifier + (bonusDamagePerCP * 1), 180*dmgMultiplier + 185 + modifier + (bonusDamagePerCP * 1))
            2 -> Pair(60*dmgMultiplier + 370 + modifier + (bonusDamagePerCP * 2), 180*dmgMultiplier + 370 + modifier + (bonusDamagePerCP * 2))
            3 -> Pair(60*dmgMultiplier + 555 + modifier + (bonusDamagePerCP * 3), 180*dmgMultiplier + 555 + modifier + (bonusDamagePerCP * 3))
            4 -> Pair(60*dmgMultiplier + 740 + modifier + (bonusDamagePerCP * 4), 180*dmgMultiplier + 740 + modifier + (bonusDamagePerCP * 4))
            5 -> Pair(60*dmgMultiplier + 925 + modifier + (bonusDamagePerCP * 5), 180*dmgMultiplier + 925 + modifier + (bonusDamagePerCP * 5))
            else -> Pair(0.0, 0.0)
        }

    }
}
