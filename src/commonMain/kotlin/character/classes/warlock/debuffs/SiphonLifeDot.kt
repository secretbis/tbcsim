package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration

class SiphonLifeDot : Debuff() {
    companion object {
        const val name = "Siphon Life (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 30000
    override val tickDeltaMs: Int = 3000

    val siphon = object : Ability() {
        override val id: Int = 30911
        override val name: String = Companion.name
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 63.0
        val numTicks = 10.0
        // TODO: What the heck school is this spell anyway
        val school = Constants.DamageType.SHADOW
        override fun cast(sim: SimIteration) {
            val spellPowerCoeff = 0.5 / numTicks
            val damageRoll = Spell.baseDamageRoll(sim, dmgPerTick, spellPowerCoeff, school)

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            sim.logEvent(event)
        }
    }

    override fun tick(sim: SimIteration) {
        siphon.cast(sim)
    }
}
