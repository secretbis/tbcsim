package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event

import sim.SimParticipant

class ImmolateDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Immolate (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 15000
    override val tickDeltaMs: Int = 3000

    val immolateDot = object : Ability() {
        override val id: Int = 27215
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 41.0
        val numTicks = 5.0
        val school = Constants.DamageType.FIRE
        override fun cast(sp: SimParticipant) {
            // Per lock discord
            val spellPowerCoeff = 0.65 / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, spellPowerCoeff, school)

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.FIRE_DAMAGE), listOf(), this, event)
        }
}
    override fun tick(sp: SimParticipant) {
        immolateDot.cast(sp)
    }
}
