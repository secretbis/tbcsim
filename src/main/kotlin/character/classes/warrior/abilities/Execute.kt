package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedExecute
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Execute : Ability() {
    companion object {
        const val name = "Execute"
    }

    override val id: Int = 12292
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun available(sim: SimIteration): Boolean {
        return sim.isExecutePhase() && super.available(sim)
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double {
        val impExRanks = sim.subject.klass.talents[ImprovedExecute.name]?.currentRank
        val discount = when(impExRanks) {
            1 -> 2
            2 -> 5
            else -> 0
        }
        return 15.0 - discount
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        val damage = 925.0 * sim.resource.currentAmount * 21
        val result = Melee.attackRoll(sim, damage, isWhiteDmg = false, isOffHand = false)

        // Save last hit state and fire event
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        ))

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
            sim.fireProc(triggerTypes, listOf(sim.subject.gear.mainHand), this)
        }
    }
}
