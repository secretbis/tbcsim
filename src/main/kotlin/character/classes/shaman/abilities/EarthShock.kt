package character.classes.shaman.abilities

import character.Ability
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration
import kotlin.random.Random

class EarthShock(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25454
    override val name: String = "Earth Shock"

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = sim.subject.spellGcd().toInt()

    val baseDamage = Pair(658.0, 693.0)
    override fun cast(free: Boolean) {
        super.cast(free)

        val school = Constants.DamageType.NATURE
        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spellPowerCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school)

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            ability = this,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off Nature damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this)
        }
    }
}
