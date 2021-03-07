package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import data.Constants
import sim.Event
import sim.SimIteration

class SiphonLife : Debuff() {
    companion object {
        const val name = "Siphon Life"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 30000
    override val tickDeltaMs: Int = 3000

    val siphon = object : Ability() {
        override val id: Int = 30911
        override val name: String = Companion.name
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 63.0
        // TODO: What the heck school is this spell anyway
        val school = Constants.DamageType.PHYSICAL
        override fun cast(sim: SimIteration) {
            // Does this actually just tick for 63 damage?  Seriously?
            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = dmgPerTick,
                result = Event.Result.HIT,
            )
            sim.logEvent(event)
        }
    }

    override fun tick(sim: SimIteration) {
        siphon.cast(sim)
    }
}
