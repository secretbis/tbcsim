package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.debuffs.*
import mechanics.Rating
import mechanics.Spell

class Rupture : FinisherAbility() {
    companion object {
        const val name = "Rupture"
    }

    override val id: Int = 26867
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 25.0

    override fun available(sp: SimParticipant): Boolean {
        // make sure we are allowed to replace existing debuff
        val wouldBeCombopoints = sp.resources[Resource.Type.COMBO_POINT]!!.currentAmount
        val wouldBeDebuff = character.classes.rogue.debuffs.RuptureDot(sp, wouldBeCombopoints)
        val canAddDebuff = sp.shouldApplyBuff(wouldBeDebuff, sp.buffs)
        
        return canAddDebuff && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        val result = Melee.attackRoll(sp, 0.0, null, isWhiteDmg = false, noDodgeAllowed = noDodgeAllowed(sp))

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = Constants.DamageType.PHYSICAL,
            abilityName = name,
            comboPointsSpent = consumedComboPoints,
            amount = 0.0,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != Event.Result.MISS && result.second != Event.Result.DODGE) {
            sp.sim.target.addDebuff(character.classes.rogue.debuffs.RuptureDot(sp, consumedComboPoints))
        }

        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT)
            Event.Result.CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT) // can't crit
            Event.Result.MISS -> listOf(Proc.Trigger.MELEE_MISS)
            Event.Result.DODGE -> listOf(Proc.Trigger.MELEE_DODGE)
            Event.Result.PARRY -> listOf(Proc.Trigger.MELEE_PARRY)
            Event.Result.BLOCK -> listOf(Proc.Trigger.MELEE_YELLOW_HIT)
            Event.Result.BLOCKED_CRIT -> listOf(Proc.Trigger.MELEE_YELLOW_HIT) // can't crit
            else -> null
        }

        fireProcAsFinisher(sp, triggerTypes, null, event)
    }
}
