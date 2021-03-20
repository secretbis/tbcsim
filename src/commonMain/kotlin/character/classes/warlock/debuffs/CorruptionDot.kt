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
import sim.SimParticipant

class CorruptionDot(owner: SimParticipant) : Debuff(owner) {
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
        override fun percentChance(sp: SimParticipant): Double {
            val nightfallRanks = sp.character.klass.talents[Nightfall.name]?.currentRank ?: 0
            return 2.0 * nightfallRanks
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.logEvent(Event(
                eventType = Event.Type.PROC,
                abilityName = Nightfall.name
            ))

            sp.addBuff(object: Buff() {
                override val name: String = Nightfall.name
                override val durationMs: Int = 10000
            })
        }
    }

    val dot = object : Ability() {
        override val id: Int = 27216
        override val name: String = Companion.name
        override fun gcdMs(sp: SimParticipant): Int = 0

        val dmgPerTick = 50.0
        val numTicks = 6.0
        val school = Constants.DamageType.SHADOW
        override fun cast(sp: SimParticipant) {
            val impCorruption = owner.character.klass.talents[EmpoweredCorruption.name] as EmpoweredCorruption?
            val bonusSpellPowerMultiplier = impCorruption?.corruptionSpellDamageMultiplier() ?: 1.0

            val contagion = owner.character.klass.talents[Contagion.name] as Contagion?
            val contagionMultiplier = contagion?.additionalDamageMultiplier() ?: 1.0

            val spellPowerCoeff = Spell.spellPowerCoeff(0, durationMs) / numTicks
            val damageRoll = Spell.baseDamageRollSingle(owner, dmgPerTick, spellPowerCoeff, school, bonusSpellDamageMultiplier = bonusSpellPowerMultiplier) * contagionMultiplier

            val event = Event(
                eventType = Event.Type.DAMAGE,
                damageType = school,
                abilityName = name,
                amount = damageRoll,
                result = Event.Result.HIT
            )
            owner.logEvent(event)

            owner.fireProc(
                listOf(Proc.Trigger.WARLOCK_TICK_CORRUPTION, Proc.Trigger.SHADOW_DAMAGE_PERIODIC),
                listOf(),
                this,
                event
            )
        }
    }

    override fun tick(sp: SimParticipant) {
        dot.cast(sp)
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(nightfallProc)
}
