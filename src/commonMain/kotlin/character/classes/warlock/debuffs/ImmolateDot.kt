package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event

import sim.SimIteration

class ImmolateDot : Debuff() {
    companion object {
        const val name = "Immolate (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 15000
    override val tickDeltaMs: Int = 3000

    val immolateDot = object : Ability() {
        override val id: Int = 27215
        override val name: String = Companion.name
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 41.0
        val numTicks = 5.0
        val school = Constants.DamageType.FIRE
        override fun cast(sim: SimIteration) {
            // Per lock discord
            val spellPowerCoeff = 0.65 / numTicks
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
        immolateDot.cast(sim)
    }
}
