package character.auto

import character.Ability
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Melee
import sim.*

abstract class AutoAttackBase(val fireIncomingEvents: Boolean = false) : Ability() {
    open val damageType = Constants.DamageType.PHYSICAL

    abstract fun item(sp: SimParticipant): Item

    override val icon: String = "ability_meleedamage.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    class AutoAttackState : Ability.State() {
        var lastAttackTimeMs = 0
        var count = 0
    }

    // Reset the timer to the start of the swing, e.g. if casting Slam
    fun resetSwingTimer(sp: SimParticipant) {
        (state(sp) as AutoAttackState).lastAttackTimeMs = sp.sim.elapsedTimeMs
    }

    // Reduce the MH swing timer by 40%, to a minimum of 20% of the total current swing timer
    // OH timer is not affected by parry haste
    fun parryHaste(sp: SimParticipant) {
        val weaponSpeedMs = sp.weaponSpeed(item(sp))
        val lastAttackMs = (state(sp) as AutoAttackState).lastAttackTimeMs
        val nextAvailableMs = nextAvailableTimeMs(sp)

        val remainingSwingMs = nextAvailableMs - lastAttackMs
        val minSwingRemainingMs = weaponSpeedMs * 0.2
        val hastedMs = maxOf(minSwingRemainingMs, remainingSwingMs * 0.4).toInt()
        (state(sp) as AutoAttackState).lastAttackTimeMs = lastAttackMs - hastedMs
    }

    override fun stateFactory(): AutoAttackState {
        return AutoAttackState()
    }

    fun nextAvailableTimeMs(sp: SimParticipant): Int {
        return (state(sp) as AutoAttackState).lastAttackTimeMs + sp.weaponSpeed(item(sp)).toInt()
    }

    override fun available(sp: SimParticipant): Boolean {
        val nextAvailableTimeMs = nextAvailableTimeMs(sp)
        return nextAvailableTimeMs <= sp.sim.elapsedTimeMs
    }

    override fun cast(sp: SimParticipant) {
        val damageRoll = Melee.baseDamageRoll(sp, item(sp))
        val result = Melee.attackRoll(sp, damageRoll, item(sp), isWhiteDmg = true)

        // Save last hit state and fire event
        (state(sp) as AutoAttackState).lastAttackTimeMs = sp.sim.elapsedTimeMs
        (state(sp) as AutoAttackState).count += 1

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = damageType,
            isWhiteDamage = true,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off a white hit
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.MELEE_AUTO_CRIT, Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRUSH -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.MELEE_AUTO_CRUSH, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.GLANCE -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_GLANCE, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_AUTO_HIT, Proc.Trigger.MELEE_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_AUTO_CRIT, Proc.Trigger.MELEE_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item(sp)), this, event)
        }

        // If configured, re-fire these events on the participant's target
        val incomingTriggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.INCOMING_MELEE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.INCOMING_MELEE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRUSH -> listOf(Proc.Trigger.INCOMING_MELEE_CRUSH, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.INCOMING_MELEE_MISS)
            EventResult.GLANCE -> listOf(Proc.Trigger.INCOMING_MELEE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.DODGE -> listOf(Proc.Trigger.INCOMING_MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.INCOMING_MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.INCOMING_MELEE_BLOCK, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.INCOMING_MELEE_BLOCK, Proc.Trigger.INCOMING_MELEE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(incomingTriggerTypes != null) {
            sp.target().fireProc(incomingTriggerTypes, listOf(item(sp)), this, event)
        }
    }
}
