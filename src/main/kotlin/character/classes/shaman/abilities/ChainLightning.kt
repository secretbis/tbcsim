package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.shaman.talents.*
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimIteration

open class ChainLightning : Ability() {
    companion object {
        const val name = "Chain Lightning"
    }

    override val id: Int = 25442
    override val name: String = Companion.name

    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun cooldownMs(sim: SimIteration): Int = 6000

    override fun resourceCost(sim: SimIteration): Double {
        val convection = sim.subject.klass.talents[Convection.name] as Convection?
        val cvRed = convection?.lightningAndShockCostReduction() ?: 0.0

        val mq = sim.subject.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val eleFocus = sim.buffs.find { it.name == ElementalFocus.name }
        val elefRed = if(eleFocus != null) { 0.40 } else 0.0

        return General.resourceCostReduction(760.0, listOf(cvRed, mqRed, elefRed))
    }

    val loBuff = object : Buff() {
        override val name: String = "Lightning Overload (CL)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHAMAN_CAST_CHAIN_LIGHTNING
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sim: SimIteration): Double {
                val lo = sim.subject.klass.talents[LightningOverload.name] as LightningOverload?
                return (lo?.currentRank ?: 0) * 4.0
            }

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                actualCast(sim, true)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    val baseCastTimeMs = 2000
    override fun castTimeMs(sim: SimIteration): Int {
        val lm = sim.subject.klass.talents[LightningMastery.name] as LightningMastery?
        return baseCastTimeMs - (lm?.lightningCastReductionAmountMs() ?: 0)
    }

    val baseDamage = Pair(734.0, 838.0)
    override fun cast(sim: SimIteration) {
        actualCast(sim)
    }

    private fun actualCast(sim: SimIteration, isLoProc: Boolean = false) {
        val loMod = if(isLoProc) { 0.5 } else 1.0

        val cot = sim.subject.klass.talents[CallOfThunder.name] as CallOfThunder?
        val cotAddlCrit = cot?.additionalLightningCritChance() ?: 0.0

        val tm = sim.subject.klass.talents[TidalMastery.name] as TidalMastery?
        val tmAddlCrit = tm?.additionalLightningAndHealingCritChance() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.NATURE

        val concussion = sim.subject.klass.talents[Concussion.name] as Concussion?
        val concussionMod = concussion?.shockAndLightningMultiplier() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sim, baseDamage.first, baseDamage.second, spellPowerCoeff, school) * concussionMod * loMod
        val result = Spell.attackRoll(sim, damageRoll, school, isBinary = false, cotAddlCrit + tmAddlCrit)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = if(isLoProc) { "Lightning Overload (CL)" } else name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Proc anything that can proc off Nature damage
        val baseTriggerTypes = if(isLoProc) { listOf() } else listOf(Proc.Trigger.SHAMAN_CAST_CHAIN_LIGHTNING)
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(loBuff)
}
