package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedWhirlwind
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Whirlwind : Ability() {
    companion object {
        const val name = "Whirlwind"
    }

    override val id: Int = 8989
    override val name: String = Companion.name

    override fun cooldownMs(sim: SimIteration): Int {
        val impWWRanks = sim.subject.klass.talents[ImprovedWhirlwind.name]?.currentRank ?: 0
        val discount = 1000 * impWWRanks
        return 6000 - discount
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 30.0

    override fun cast(sim: SimIteration, free: Boolean) {
        val damageRoll = Melee.baseDamageRoll(sim, sim.subject.gear.mainHand, isNormalized = true)
        val result = Melee.attackRoll(sim, damageRoll, isWhiteDmg = false, isOffHand = false)

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

    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
}
