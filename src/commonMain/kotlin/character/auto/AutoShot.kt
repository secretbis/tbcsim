package character.auto

import character.Proc
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Ranged
import sim.Event
import sim.SimParticipant


class AutoShot : AutoAttackBase() {
    companion object {
        const val name = "Auto Shot"
    }

    override fun item(sp: SimParticipant): Item {
        return sp.character.gear.rangedTotemLibram
    }

    override val id: Int = 1
    override val name: String = "Auto Shot"

    override fun cast(sp: SimParticipant) {
        // Ammo adds DPS, so we can just model it as bonus AP
        val ammoBonusAp = General.dpsToAp(sp.character.gear.ammo.maxDmg)
        val damageRoll = Ranged.baseDamageRoll(sp, item(sp), bonusAp = ammoBonusAp, isWhiteDmg = true)
        val result = Ranged.attackRoll(sp, damageRoll, item(sp), isWhiteDmg = true)

        // Save last hit state and fire event
        (state(sp) as AutoAttackState).lastAttackTimeMs = sp.sim.elapsedTimeMs
        (state(sp) as AutoAttackState).count += 1

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            isWhiteDamage = true,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.RANGED_AUTO_HIT, Proc.Trigger.RANGED_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.RANGED_AUTO_CRIT, Proc.Trigger.RANGED_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.MISS -> listOf(Proc.Trigger.RANGED_MISS)
            Event.Result.BLOCK -> listOf(Proc.Trigger.RANGED_AUTO_HIT, Proc.Trigger.RANGED_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.RANGED_AUTO_CRIT, Proc.Trigger.RANGED_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item(sp)), this, event)
        }
    }
}
