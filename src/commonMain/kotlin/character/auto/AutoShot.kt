package character.auto

import character.Proc
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Ranged
import sim.Event
import sim.EventResult
import sim.EventType
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
    override val icon: String = "ability_whirlwind.jpg"
    override fun castTimeMs(sp: SimParticipant): Int {
        return (500.0 / sp.physicalHasteMultiplier()).toInt()
    }

    override fun cast(sp: SimParticipant) {
        // Ammo adds DPS, so we can just model it as bonus AP
        val ammoBonusAp = General.dpsToAp(sp.character.gear.ammo.maxDmg)
        val damageRoll = Ranged.baseDamageRoll(sp, item(sp), bonusAp = ammoBonusAp)
        val result = Ranged.attackRoll(sp, damageRoll, item(sp), isWhiteDmg = true)

        // Save last hit state and fire event
        (state(sp) as AutoAttackState).lastAttackTimeMs = sp.sim.elapsedTimeMs
        (state(sp) as AutoAttackState).count += 1

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            isWhiteDamage = true,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.RANGED_AUTO_HIT, Proc.Trigger.RANGED_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.RANGED_AUTO_CRIT, Proc.Trigger.RANGED_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.RANGED_MISS)
            EventResult.BLOCK -> listOf(Proc.Trigger.RANGED_AUTO_HIT, Proc.Trigger.RANGED_WHITE_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.RANGED_AUTO_CRIT, Proc.Trigger.RANGED_WHITE_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item(sp)), this, event)
        }
    }
}
