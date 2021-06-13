package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import character.classes.warlock.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class CurseOfAgonyDot(owner: SimParticipant) : Debuff(owner) {
    companion object {
        const val name = "Curse of Agony (DoT)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 24000
    override val tickDeltaMs: Int = 2000

    // DoT behavior: first 4 ticks deal 0.5x tick damage, next 4 do normal, last 4 do 1.5x

    val dot = object : Ability() {
        override val id: Int = 27218
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 113.0
        val numTicks = durationMs / tickDeltaMs
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            val impCoa = owner.character.klass.talents[ImprovedCurseOfAgony.name] as ImprovedCurseOfAgony?
            val impCoaMultiplier = impCoa?.damageMultiplier() ?: 1.0

            val contagion = owner.character.klass.talents[Contagion.name] as Contagion?
            val contagionMultiplier = contagion?.additionalDamageMultiplier() ?: 1.0

            // Per lock discord
            val spellPowerCoeff = 1.0 / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, school, spellPowerCoeff) * contagionMultiplier * impCoaMultiplier

            val event = Event(
                eventType = EventType.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = EventResult.HIT,
            )
            owner.logEvent(event)

            owner.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sp: SimParticipant) {
        dot.cast(owner)
    }
}
