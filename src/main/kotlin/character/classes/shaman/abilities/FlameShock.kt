package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.debuffs.FlameShockDot
import character.classes.shaman.talents.Reverberation
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
    override fun cooldownMs(sim: SimIteration): Int {
        val reverberation = sim.subject.klass.talents[Reverberation.name] as Reverberation?
        return 6000 - (200 * (reverberation?.currentRank ?: 0))
    }
    override val sharedCooldown: SharedCooldown = SharedCooldown.SHAMAN_SHOCK
    override fun gcdMs(sim: SimIteration): Int = sim.subject.spellGcd().toInt()

    val baseDamage = 377.0
    override fun cast(sim: SimIteration, free: Boolean) {
        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.FIRE
        val damageRoll = Spell.baseDamageRoll(sim, baseDamage, spellPowerCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school)

        sim.logEvent(Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        ))

        // Apply the DoT
        sim.addDebuff(FlameShockDot())

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
