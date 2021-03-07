package character.classes.shaman.debuffs

import character.Ability
import character.Debuff
import character.Proc
import character.Stats
import data.Constants
import mechanics.Spell
import sim.Event

import sim.SimIteration

class FlameShockDot : Debuff() {
    override val name: String = "Flame Shock (DoT)"
    override val durationMs: Int = 12000
    override val tickDeltaMs: Int = 3000

    val fsdAbility = object : Ability() {
        override val id: Int = 25457
        override val name: String = "Flame Shock (DoT)"
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 105.0
        val numTicks = 4.0
        val school = Constants.DamageType.FIRE
        override fun cast(sim: SimIteration) {
            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks
            val damageRoll = Spell.baseDamageRoll(sim, dmgPerTick, spellPowerCoeff, school)

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            sim.logEvent(event)

            sim.fireProc(listOf(Proc.Trigger.FIRE_DAMAGE), listOf(), this, event)
        }
    }

    override fun tick(sim: SimIteration) {
        fsdAbility.cast(sim)
    }
}
