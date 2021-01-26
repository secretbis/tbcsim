package character.auto

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.Event
import sim.SimIteration

abstract class MeleeBase(sim: SimIteration) : Ability(sim) {
    abstract val item: Item
    abstract val isOffhand: Boolean

    fun weaponSpeed(): Double {
        return (item.speed / sim.subject.meleeHasteMultiplier()).coerceAtLeast(0.01)
    }

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = 0

    class AutoAttackState : Ability.State() {
        var lastAttackTimeMs = 0
    }

    override fun stateFactory(): AutoAttackState {
        return AutoAttackState()
    }

    override fun available(sim: SimIteration): Boolean {
        val nextAvailableTimeMs = (state(sim) as AutoAttackState).lastAttackTimeMs + weaponSpeed()
        return nextAvailableTimeMs <= sim.elapsedTimeMs
    }

    override fun cast(free: Boolean) {
        val damageRoll = Melee.baseDamageRoll(sim, item)
        val result = Melee.attackRoll(sim, damageRoll, true, isOffhand)

        // Save last hit state and fire event
        (state(sim) as AutoAttackState).lastAttackTimeMs = sim.elapsedTimeMs
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off a white hit
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_AUTO_CRIT, Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.GLANCE -> listOf(Proc.Trigger.MELEE_GLANCE)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_AUTO_CRIT, Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this)
        }
    }
}
