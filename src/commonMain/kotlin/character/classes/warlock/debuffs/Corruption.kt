package character.classes.warlock.debuffs

import character.Ability
import character.Buff
import character.Debuff
import character.Proc
import character.classes.warlock.talents.Contagion
import character.classes.warlock.talents.EmpoweredCorruption
import character.classes.warlock.talents.Nightfall
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.SimIteration

class Corruption : Debuff() {
    companion object {
        const val name = "Corruption (DoT)"
    }

    override val name: String = Companion.name
    override val durationMs: Int = 18000
    override val tickDeltaMs: Int = 3000

    val nightfallProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.WARLOCK_TICK_CORRUPTION
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sim: SimIteration): Double {
            val nightfallRanks = sim.subject.klass.talents[Nightfall.name]?.currentRank ?: 0
            return 0.02 * nightfallRanks
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(object: Buff() {
                override val name: String = Nightfall.name
                override val durationMs: Int = 10000
                override val hidden: Boolean = true
            })
        }
    }

    val dot = object : Ability() {
        override val id: Int = 27216
        override val name: String = Companion.name
        override fun gcdMs(sim: SimIteration): Int = 0

        val dmgPerTick = 50.0
        val numTicks = 6.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sim: SimIteration) {
            val impCorruption = sim.subject.klass.talents[EmpoweredCorruption.name] as EmpoweredCorruption?
            val bonusSpellPowerMultiplier = impCorruption?.corruptionSpellDamageMultiplier() ?: 1.0

            val contagion = sim.subject.klass.talents[Contagion.name] as Contagion?
            val contagionMultiplier = contagion?.additionalDamageMultiplier() ?: 1.0

            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks
            val damageRoll = Spell.baseDamageRoll(sim, dmgPerTick, spellPowerCoeff, school, bonusSpellDamageMultiplier = bonusSpellPowerMultiplier) * contagionMultiplier

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT,
            )
            sim.logEvent(event)

            sim.fireProc(
                listOf(Proc.Trigger.WARLOCK_TICK_CORRUPTION, Proc.Trigger.SHADOW_DAMAGE_PERIODIC),
                listOf(),
                this,
                event
            )
        }
    }

    override fun tick(sim: SimIteration) {
        dot.cast(sim)
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(nightfallProc)
}
