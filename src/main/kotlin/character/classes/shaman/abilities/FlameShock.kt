package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.debuffs.FlameShockDot
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration

class FlameShock : Ability() {
    companion object {
        const val name = "Flame Shock"
    }

    override val id: Int = 25457
    override val name: String = Companion.name

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.subject.spellGcd().toInt()

    val baseDamage = 377.0
    override fun cast(sim: SimIteration, free: Boolean) {
        val school = Constants.DamageType.FIRE
        val damageRoll = Spell.baseDamageRoll(sim, baseDamage, baseDamage, spellPowerCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school)

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        ))

        // Proc the DoT
        FlameShockDot().procs(sim).forEach { it.proc(sim, listOf(), this) }

        // Proc anything that can proc off Fire damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this)
        }
    }
}
