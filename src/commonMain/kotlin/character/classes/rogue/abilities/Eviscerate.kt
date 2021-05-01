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
import kotlinx.coroutines.channels.consumesAll

class Eviscerate : FinisherAbility() {
    companion object {
        const val name = "Eviscerate"
    }

    override val id: Int = 26865
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 35.0

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
        val modifier = sp.attackPower() * (cps * 0.03)
        return when(consumedComboPoints){
            1 -> Pair(60*dmgMultiplier + 185 + modifier, 180*dmgMultiplier + 185 + modifier)
            2 -> Pair(60*dmgMultiplier + 370 + modifier, 180*dmgMultiplier + 370 + modifier)
            3 -> Pair(60*dmgMultiplier + 555 + modifier, 180*dmgMultiplier + 555 + modifier)
            4 -> Pair(60*dmgMultiplier + 740 + modifier, 180*dmgMultiplier + 740 + modifier)
            5 -> Pair(60*dmgMultiplier + 925 + modifier, 180*dmgMultiplier + 925 + modifier)
            else -> Pair(0.0, 0.0)
        }

    }
}
