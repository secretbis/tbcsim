package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class UnstableAfflictionDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Unstable Affliction (DoT)"
    }
    override val name: String = Companion.name
    override val durationMs: Int = 18000
    override val tickDeltaMs: Int = 3000

    val dmgPerTick = 175.0
    val numTicks = 6.0
    val school = Constants.DamageType.SHADOW
    val ua = object : Ability() {
        override val id: Int = 30405
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        override fun cast(sp: SimParticipant) {
            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, spellPowerCoeff, school)

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        ua.cast(owner)
    }
}
