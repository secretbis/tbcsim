package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedSlam
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimIteration

class Slam : Ability() {
    companion object {
        const val name = "Slam"
    }

    override val id: Int = 25242
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun castTimeMs(sim: SimIteration): Int {
        val impSlamRanks = sim.subject.klass.talents[ImprovedSlam.name]?.currentRank ?: 0
        return 1500 - (500 * impSlamRanks)
    }

    override fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sim: SimIteration): Double = 15.0

    override fun cast(sim: SimIteration) {
        val item = sim.subject.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sim, item, isNormalized = false)
        val result = Melee.attackRoll(sim, damageRoll, item, isWhiteDmg = false)

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

        // Reset MH swing timer
        sim.mhAutoAttack?.resetSwingTimer(sim)

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
