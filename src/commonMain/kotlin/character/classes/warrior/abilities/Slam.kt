package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedSlam
import data.Constants
import mechanics.Melee
import sim.Event
import sim.SimParticipant

class Slam : Ability() {
    companion object {
        const val name = "Slam"
    }

    override val id: Int = 25242
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun castTimeMs(sp: SimParticipant): Int {
        val impSlamRanks = sp.character.klass.talents[ImprovedSlam.name]?.currentRank ?: 0
        return 1500 - (500 * impSlamRanks)
    }

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double = 15.0

    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sp, item, isNormalized = false)
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

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
        sp.mhAutoAttack?.resetSwingTimer(sp)

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
