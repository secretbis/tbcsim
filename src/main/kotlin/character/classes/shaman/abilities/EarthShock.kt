package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.talents.Reverberation
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration
import kotlin.random.Random

class EarthShock : Ability() {
    companion object {
        const val name = "Earth Shock"
    }

    override val id: Int = 25454
    override val name: String = Companion.name

    override val baseCastTimeMs: Int = 0
    override fun cooldownMs(sim: SimIteration): Int {
        val reverberation = sim.subject.klass.talents[Reverberation.name] as Reverberation?
        return 6000 - (200 * (reverberation?.currentRank ?: 0))
    }
    override val sharedCooldown: SharedCooldown = SharedCooldown.SHAMAN_SHOCK

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    val baseDamage = Pair(658.0, 693.0)
    override fun cast(sim: SimIteration, free: Boolean) {
        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.NATURE
        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spellPowerCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school)

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        ))

        // Proc anything that can proc off Nature damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this)
        }
    }
}
