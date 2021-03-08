package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.debuffs.ImmolateDot
import character.classes.warlock.talents.Cataclysm
import character.classes.warlock.talents.Devastation
import character.classes.warlock.talents.Shadowburn
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimIteration

class Shadowburn : Ability() {
    companion object {
        const val name = "Shadowburn"
    }

    override val id: Int = 30546
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun cooldownMs(sim: SimIteration): Int = 15000

    override fun available(sim: SimIteration): Boolean {
        return (sim.subject.klass.talents[Shadowburn.name]?.currentRank ?: 0) > 0
    }

    override fun resourceCost(sim: SimIteration): Double {
        val cataclysm = sim.subject.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(515.0, listOf(cataRed))
    }

    val baseDamage = Pair(597.0, 666.0)
    override fun cast(sim: SimIteration) {
        val devastation = sim.subject.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.SHADOW

        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spellPowerCoeff, school)
        val result = Spell.attackRoll(sim, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Proc anything that can proc off non-periodic Fire damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
