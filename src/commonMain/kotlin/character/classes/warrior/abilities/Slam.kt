package character.classes.warrior.abilities

import character.*
import character.classes.warrior.talents.ImprovedSlam
import data.Constants
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
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

    val bonusDmg = 140.0
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val damageRoll = Melee.baseDamageRoll(sp, item, isNormalized = false) + bonusDmg
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off a yellow hit
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT, Proc.Trigger.PHYSICAL_DAMAGE)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_CRIT, Proc.Trigger.PHYSICAL_DAMAGE)
            else -> null
        }

        // Reset MH swing timer
        sp.mhAutoAttack?.resetSwingTimer(sp)

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
