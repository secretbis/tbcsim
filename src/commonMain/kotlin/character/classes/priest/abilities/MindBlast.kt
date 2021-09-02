package character.classes.priest.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class MindBlast : Ability() {
    companion object {
        const val name: String = "Mind Blast"
    }
    override val id: Int = 25375
    override val name: String = Companion.name

    val baseCastTimeMs = 1500
    val baseDamage = Pair(711.0, 752.0)
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int = 8000
    
    override fun castTimeMs(sp: SimParticipant): Int = baseCastTimeMs

    override fun resourceCost(sp: SimParticipant): Double = 450.0    
    
    override fun cast(sp: SimParticipant) {
        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
