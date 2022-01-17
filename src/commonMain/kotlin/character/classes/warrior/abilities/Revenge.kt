package character.classes.warrior.abilities

import character.*
import character.classes.warrior.buffs.RevengeBase
import character.classes.warrior.talents.FocusedRage
import data.Constants
import mechanics.Melee
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant
import kotlin.random.Random

class Revenge : Ability() {
    companion object {
        const val name = "Revenge"
    }

    override val id: Int = 30357
    override val name: String = Companion.name
    override val icon: String = "ability_warrior_revenge.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 5000

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.RAGE
    override fun resourceCost(sp: SimParticipant): Double {
        val focusedRageRanks = sp.character.klass.talents[FocusedRage.name]?.currentRank ?: 0
        return 5.0 - focusedRageRanks
    }

    override fun available(sp: SimParticipant): Boolean {
        val isDefensiveStance = sp.buffs[DefensiveStance.name] != null
        val hasTriggerBuff = sp.buffs[RevengeBase.revengeFlagBuff.name] != null
        return isDefensiveStance && hasTriggerBuff && super.available(sp)
    }

    val baseDamage = Pair(414.0, 506.0)
    override fun cast(sp: SimParticipant) {
        val item = sp.character.gear.mainHand
        val damageRoll = Random.nextDouble(baseDamage.first, baseDamage.second)
        val result = Melee.attackRoll(sp, damageRoll, item, isWhiteDmg = false)

        // Save last hit state and fire event
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            ability = this,
            amount = result.first,
            result = result.second,
            abilityThreatMultiplier = 2.25
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

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(item), this, event)
        }
    }
}
