package character.classes.warlock.debuffs

import character.Ability
import character.Debuff
import character.Proc
import character.classes.warlock.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimIteration

class CurseOfAgonyDot : Debuff() {
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
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 113.0
        val numTicks = 12.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sim: SimIteration) {
            val impCoa = sim.subject.klass.talents[ImprovedCurseOfAgony.name] as ImprovedCurseOfAgony?
            val impCoaMultiplier = impCoa?.damageMultiplier() ?: 1.0

            val contagion = sim.subject.klass.talents[Contagion.name] as Contagion?
            val contagionMultiplier = contagion?.additionalDamageMultiplier() ?: 1.0

            // Per lock discord
            val spellPowerCoeff = 1.0 / numTicks
            val damageRoll = Spell.baseDamageRoll(sim, dmgPerTick, spellPowerCoeff, school) * contagionMultiplier * impCoaMultiplier

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            sim.logEvent(event)

            sim.fireProc(listOf(Proc.Trigger.SHADOW_DAMAGE_PERIODIC), listOf(), this, event)
        }
    }

    override fun tick(sim: SimIteration) {
        dot.cast(sim)
    }
}
