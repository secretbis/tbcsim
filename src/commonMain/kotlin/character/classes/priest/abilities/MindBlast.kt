package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.*
import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class MindBlast : Ability() {
    companion object {
        const val name: String = "Mind Blast"
    }
    override val id: Int = 25375
    override val name: String = Companion.name

    val baseCastTimeMs = 1500
    val baseDamage = Pair(711.0, 752.0)
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCooldownMs = 8000
    override fun cooldownMs(sp: SimParticipant): Int {
        val impMb: ImprovedMindBlast? = sp.character.klass.talentInstance(ImprovedMindBlast.name)
        val impMbCooldownReductionMs = impMb?.cooldownReductionMs() ?: 0

        return baseCooldownMs - impMbCooldownReductionMs
    }
    
    override fun castTimeMs(sp: SimParticipant): Int = (baseCastTimeMs / sp.spellHasteMultiplier()).toInt()

    val baseResourceCost = 450.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        val fm: FocusedMind? = sp.character.klass.talentInstance(FocusedMind.name)
        val fmMulti = fm?.manaReductionMultiplier() ?: 1.0

        return baseResourceCost * fmMulti
    }
    
    override fun cast(sp: SimParticipant) {
        val spTalent: ShadowPower? = sp.character.klass.talentInstance(ShadowPower.name)
        val spCrit = spTalent?.critIncreasePct() ?: 0.0
        val sfTalent: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = sfTalent?.shadowHitIncreasePct() ?: 0.0
        val targetMultiplier = sp.sim.target.stats.getSpellDamageTakenMultiplier(school)
        
        val damageRoll = Spell.baseDamageRoll(
            sp, 
            baseDamage.first,
            baseDamage.second, 
            school, 
            spellPowerCoeff,
        )
        val result = Spell.attackRoll(sp, damageRoll, school, bonusHitChance = sfHit, bonusCritChance = spCrit )
        var finalDamage = result.first * targetMultiplier;

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = finalDamage,
            result = result.second,
        )
        sp.logEvent(event)

        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
