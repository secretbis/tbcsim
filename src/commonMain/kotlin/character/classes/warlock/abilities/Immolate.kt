package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.debuffs.ImmolateDot
import character.classes.warlock.talents.Bane
import character.classes.warlock.talents.Cataclysm
import character.classes.warlock.talents.Devastation
import character.classes.warlock.talents.ImprovedImmolate
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimIteration

class Immolate : Ability() {
    companion object {
        const val name = "Immolate"
    }
    override val id: Int = 32231
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        val cataclysm = sim.subject.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(445.0, listOf(cataRed))
    }

    val baseCastTimeMs = 2000
    override fun castTimeMs(sim: SimIteration): Int {
        val bane = sim.subject.klass.talents[Bane.name] as Bane?
        return baseCastTimeMs - (bane?.destructionCastReductionAmountMs() ?: 0)
    }

    val baseDamage = 327.0
    override fun cast(sim: SimIteration) {
        val devastation = sim.subject.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val impImmolate = sim.subject.klass.talents[ImprovedImmolate.name] as ImprovedImmolate?
        val impImmolateInitialMultiplier = impImmolate?.immolateInitialDamageMultiplier() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.FIRE

        val damageRoll = Spell.baseDamageRoll(sim, baseDamage, spellPowerCoeff, school) * impImmolateInitialMultiplier
        val result = Spell.attackRoll(sim, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Apply the DoT
        sim.addDebuff(ImmolateDot())

        // Proc anything that can proc off non-periodic Fire damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
