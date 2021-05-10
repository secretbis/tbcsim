package character.classes.hunter.pet.abilities

import character.Ability
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class LightningBreathRank6 : Ability() {
    override val id: Int = 25012
    // TODO: Is there no Outland rank of this?
    override val name: String = "Lightning Breath (Rank 6)"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.FOCUS
    override fun resourceCost(sp: SimParticipant): Double = 50.0

    val baseDamage = Pair(99.0, 113.0)
    val spCoeff = 1.0
    val school = Constants.DamageType.NATURE
    override fun cast(sp: SimParticipant) {
        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, spCoeff, school)
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
