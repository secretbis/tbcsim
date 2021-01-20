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

    fun getWeaponSpeed(): Double {
        return (item.speed / (1 + sim.subject.getMeleeHastePct())).coerceAtLeast(0.01)
    }

    override fun castTimeMs(): Int = 0
    override fun gcdMs(): Int = 0

    var lastAttackTimeMs: Int = 0

    override fun available(): Boolean {
        val nextAvailableTimeMs = lastAttackTimeMs + getWeaponSpeed()
        return nextAvailableTimeMs <= sim.elapsedTimeMs
    }

    override fun cast(free: Boolean) {
        val damageRoll = Melee.baseDamageRoll(sim, item)
        val result = Melee.attackRoll(sim, damageRoll, true)

        // Save last hit state and fire event
        lastAttackTimeMs = sim.elapsedTimeMs
        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off a white hit
        // TODO: Procs off miss/dodge/parry/etc?
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_WHITE_HIT)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_WHITE_CRIT)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(item), this)
        }
    }
}
