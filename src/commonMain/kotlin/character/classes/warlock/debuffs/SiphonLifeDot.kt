package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class SiphonLifeDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Siphon Life (DoT)"
    }
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_requiem.jpg"
    override val durationMs: Int = 30000
    override val tickDeltaMs: Int = 3000

    val siphon = object : Ability() {
        override val id: Int = 30911
        override val name: String = Companion.name
        override val icon: String = "spell_shadow_requiem.jpg"
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 63.0
        val numTicks = durationMs / tickDeltaMs
        // TODO: What the heck school is this spell anyway
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = 0.5 / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff)

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                ability = this,
                amount = damageRoll,
                result = EventResult.HIT,
            )
            owner.logEvent(event)
        }
    }

    override fun tick(sp: SimParticipant) {
        siphon.cast(sp)
    }
}
