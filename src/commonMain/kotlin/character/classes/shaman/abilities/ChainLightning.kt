package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.shaman.talents.*
import data.Constants
import data.model.Item
import mechanics.General
import mechanics.Spell
import sim.*

open class ChainLightning : Ability() {
    companion object {
        const val name = "Chain Lightning"
    }

    override val id: Int = 25442
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int = 6000

    override fun resourceCost(sp: SimParticipant): Double {
        val convection = sp.character.klass.talents[Convection.name] as Convection?
        val cvRed = convection?.lightningAndShockCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val eleFocus = sp.buffs[ElementalFocus.name]
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
            override fun percentChance(sp: SimParticipant): Double {
                val lo = sp.character.klass.talents[LightningOverload.name] as LightningOverload?
                return (lo?.currentRank ?: 0) * 4.0
            }

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                actualCast(sp, true)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val baseCastTimeMs = 2000
    override fun castTimeMs(sp: SimParticipant): Int {
        val lm = sp.character.klass.talents[LightningMastery.name] as LightningMastery?
        return ((baseCastTimeMs - (lm?.lightningCastReductionAmountMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()
    }

    val baseDamage = Pair(734.0, 838.0)
    override fun cast(sp: SimParticipant) {
        actualCast(sp)
    }

    private fun actualCast(sp: SimParticipant, isLoProc: Boolean = false) {
        val loMod = if(isLoProc) { 0.5 } else 1.0

        val cot = sp.character.klass.talents[CallOfThunder.name] as CallOfThunder?
        val cotAddlCrit = cot?.additionalLightningCritChance() ?: 0.0

        val tm = sp.character.klass.talents[TidalMastery.name] as TidalMastery?
        val tmAddlCrit = tm?.additionalLightningAndHealingCritChance() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.NATURE

        val concussion = sp.character.klass.talents[Concussion.name] as Concussion?
        val concussionMod = concussion?.shockAndLightningMultiplier() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff) * concussionMod * loMod
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false, cotAddlCrit + tmAddlCrit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = if(isLoProc) { "Lightning Overload (CL)" } else name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off Nature damage
        val baseTriggerTypes = if(isLoProc) { listOf() } else listOf(Proc.Trigger.SHAMAN_CAST_CHAIN_LIGHTNING)
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(loBuff)
}
