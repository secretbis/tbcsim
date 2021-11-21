package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.talents.*
import data.Constants
import data.itemsets.MaleficRaiment
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

open class ShadowBolt : Ability() {
    companion object {
        const val name = "Shadow Bolt"
    }

    override val id: Int = 27209
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_shadowbolt.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val cataclysm = sp.character.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(420.0, listOf(cataRed))
    }

    val baseCastTimeMs = 3000
    override fun castTimeMs(sp: SimParticipant): Int {
        // Check for Nightfall proc
        val nightfallProc = sp.buffs[Nightfall.name]

        if(nightfallProc != null) {
            sp.consumeBuff(nightfallProc)
            return 0
        }

        // Otherwise cast normally
        val bane = sp.character.klass.talents[Bane.name] as Bane?
        return ((baseCastTimeMs - (bane?.destructionCastReductionAmountMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()
    }

    val baseDamage = Pair(541.0, 603.0)
    override fun cast(sp: SimParticipant) {
        val devastation = sp.character.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val shadowAndFlame = sp.character.klass.talents[ShadowAndFlame.name] as ShadowAndFlame?
        val shadowAndFlameBonusSpellDamageMultiplier = shadowAndFlame?.bonusDestructionSpellDamageMultiplier() ?: 1.0

        val t6Bonus = sp.buffs[MaleficRaiment.FOUR_SET_BUFF_NAME] != null
        val t6Multiplier = if(t6Bonus) { MaleficRaiment.fourSetSBIncinerateDamageMultiplier() } else 1.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.SHADOW

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff, bonusSpellDamageMultiplier = shadowAndFlameBonusSpellDamageMultiplier) * t6Multiplier
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off non-periodic Shadow damage
        val baseTriggerTypes = if(result.second == EventResult.CRIT) { listOf(Proc.Trigger.WARLOCK_CRIT_SHADOW_BOLT) } else listOf()
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.WARLOCK_HIT_SHADOW_BOLT, Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.WARLOCK_CRIT_SHADOW_BOLT, Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.WARLOCK_HIT_SHADOW_BOLT, Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.WARLOCK_CRIT_SHADOW_BOLT, Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }
}
