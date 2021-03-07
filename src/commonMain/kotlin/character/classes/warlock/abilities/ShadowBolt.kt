package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.talents.*
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimIteration

open class ShadowBolt : Ability() {
    companion object {
        const val name = "Shadow Bolt"
    }

    override val id: Int = 27209
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        val cataclysm = sim.subject.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(420.0, listOf(cataRed))
    }

    val baseCastTimeMs = 3000
    override fun castTimeMs(sim: SimIteration): Int {
        // Check for Nightfall proc
        val nightfallProc = sim.buffs[Nightfall.name]

        if(nightfallProc != null) {
            sim.consumeBuff(nightfallProc)
            return 0
        }

        // Otherwise cast normally
        val bane = sim.subject.klass.talents[Bane.name] as Bane?
        return baseCastTimeMs - (bane?.destructionCastReductionAmountMs() ?: 0)
    }

    val baseDamage = Pair(541.0, 603.0)
    override fun cast(sim: SimIteration) {
        val devastation = sim.subject.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val shadowAndFlame = sim.subject.klass.talents[ShadowAndFlame.name] as ShadowAndFlame?
        val shadowAndFlameBonusSpellDamageMultiplier = shadowAndFlame?.bonusDestructionSpellDamageMultiplier() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.SHADOW

        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spellPowerCoeff, school, bonusSpellDamageMultiplier = shadowAndFlameBonusSpellDamageMultiplier)
        val result = Spell.attackRoll(sim, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Proc anything that can proc off non-periodic Shadow damage
        val baseTriggerTypes = if(result.second == Event.Result.CRIT) { listOf(Proc.Trigger.WARLOCK_CRIT_SHADOW_BOLT) } else listOf()
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }
}
