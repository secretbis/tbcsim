package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import sim.EventResult
import sim.EventType

class Rupture : FinisherAbility() {
    companion object {
        const val name = "Rupture"
    }

    override val id: Int = 26867
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 25.0

//    TODO: This has a very large performance impact - the rotation should handle all of these cases, practically speaking
//    override fun available(sp: SimParticipant): Boolean {
//        // make sure we are allowed to replace existing debuff
//        val wouldBeCombopoints = sp.resources[Resource.Type.COMBO_POINT]!!.currentAmount
//        val wouldBeDebuff = character.classes.rogue.debuffs.RuptureDot(sp, wouldBeCombopoints)
//        val canAddDebuff = sp.shouldApplyBuff(wouldBeDebuff, sp.buffs)
//
//        return canAddDebuff && super.available(sp)
//    }

    override fun cast(sp: SimParticipant) {
        val result = Melee.attackRoll(sp, 0.0, null, isWhiteDmg = false, noDodgeAllowed = noDodgeAllowed(sp))

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            comboPointsSpent = consumedComboPoints,
            amount = 0.0,
            result = result.second,
        )
        //sp.logEvent(event)

        if(result.second != EventResult.MISS && result.second != EventResult.DODGE && result.second != EventResult.PARRY) {
            sp.sim.target.addDebuff(character.classes.rogue.debuffs.RuptureDot(sp, consumedComboPoints))
        }

        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT)
            EventResult.CRIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT) // can't crit
            EventResult.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            EventResult.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            EventResult.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            EventResult.BLOCK -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT)
            EventResult.BLOCKED_CRIT -> listOf(Proc.Trigger.ROGUE_ANY_DAMAGING_SPECIAL, Proc.Trigger.MELEE_YELLOW_HIT) // can't crit
            else -> null
        }

        fireProcAsFinisher(sp, triggerTypes, null, event)
    }
}
