package character.classes.warrior.abilities

import character.Ability
import character.Proc
import character.Resource
import character.classes.warrior.talents.Bloodthirst as BloodthirstTalent
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Bloodthirst : Ability() {
    companion object {
        const val name: String = "Bloodthirst"
    }

    override val id: Int = 30335
    override val name: String = Companion.name

    override fun cooldownMs(sim: SimIteration): Int = 6000
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 30.0

    override fun available(sim: SimIteration): Boolean {
        return sim.subject.klass.talents[BloodthirstTalent.name]?.currentRank == 1 && super.available(sim)
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        val damage = sim.attackPower() * 0.45
        val result = Melee.attackRoll(sim, damage, isWhiteDmg = false, isOffHand = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Proc anything that can proc off a yellow hit
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

        if(triggerTypes != null) {
            // TODO: This currently assigns the main hand weapon as context,
            //       since that would allow things like Sword Spec to proc off of BT, which I presume it can
            sim.fireProc(triggerTypes, listOf(sim.subject.gear.mainHand), this, event)
        }
    }
}
