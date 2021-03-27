package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.talents.*
import data.Constants
import data.itemsets.MaleficRaiment
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class Incinerate : Ability() {
    companion object {
        const val name = "Incinerate"
    }

    override val id: Int = 32231
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val cataclysm = sp.character.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(355.0, listOf(cataRed))
    }

    val baseCastTimeMs = 2500
    override fun castTimeMs(sp: SimParticipant): Int {
        val emberstorm = sp.character.klass.talents[Emberstorm.name] as Emberstorm?
        return (baseCastTimeMs * (emberstorm?.incinerateCastTimeMultiplier() ?: 1.0)).toInt()
    }

    val baseDamage = Pair(444.0, 515.0)
    val baseDamageWithImmolate = Pair(baseDamage.first + 111, baseDamage.second + 182)
    override fun cast(sp: SimParticipant) {
        val devastation = sp.character.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val shadowAndFlame = sp.character.klass.talents[ShadowAndFlame.name] as ShadowAndFlame?
        val shadowAndFlameBonusSpellDamageMultiplier = shadowAndFlame?.bonusDestructionSpellDamageMultiplier() ?: 1.0

        val t6Bonus = sp.buffs[MaleficRaiment.FOUR_SET_BUFF_NAME] != null
        val t6Multiplier = if(t6Bonus) { MaleficRaiment.fourSetSBIncinerateDamageMultiplier() } else 1.0

        val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
        val school = Constants.DamageType.FIRE

        val hasImmolate = sp.sim.target.debuffs[Immolate.name] !== null
        val damagePair = if(hasImmolate) { baseDamageWithImmolate } else baseDamage

        val damageRoll = Spell.baseDamageRoll(sp, damagePair.first, damagePair.second, spellPowerCoeff, school, bonusSpellDamageMultiplier = shadowAndFlameBonusSpellDamageMultiplier) * t6Multiplier
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off non-periodic Fire damage
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.WARLOCK_HIT_INCINERATE, Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.WARLOCK_CRIT_INCINERATE, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.WARLOCK_HIT_INCINERATE, Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.WARLOCK_CRIT_INCINERATE, Proc.Trigger.FIRE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
