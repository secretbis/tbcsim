package character.classes.priest.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.priest.buffs.PowerInfusion
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.InnerFocus as InnerFocusTalent
import character.classes.priest.talents.*
import character.classes.priest.debuffs.*
import character.Resource
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWordDeath : Ability() {
    companion object {
        const val name: String = "Shadow Word Death"
    }
    override val id: Int = 32996
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 12000

    val baseResourceCost = 309.0
    override fun resourceCost(sp: SimParticipant): Double {
        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0

        val mentalAgility = sp.character.klass.talents[MentalAgility.name] as MentalAgility?
        val mentalAgilityManaCostMultiplier = mentalAgility?.instantSpellManaCostReductionMultiplier() ?: 0.0
        
        return baseResourceCost * piMult * mentalAgilityManaCostMultiplier
    }

    val baseDamage = Pair(572.0, 664.0)
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(0)
    override fun cast(sp: SimParticipant) {    
        val shadowFocus: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = shadowFocus?.shadowHitIncreasePct() ?: 0.0 

        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        val ifCrit = innerFocusBuff?.critPct() ?: 0.0      

        val shadowPower = sp.character.klass.talents[ShadowPower.name] as ShadowPower?
        val shadowPowerCritIncrease = shadowPower?.mindBlastSwDCritIncreasePct() ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)

        val shadowWeaving = sp.sim.target.debuffs.get(ShadowWeaving.name) as ShadowWeavingDebuff?
        val swMult = shadowWeaving?.shadowDamageMultiplierPct() ?: 1.0

        val result = Spell.attackRoll(sp, damageRoll * swMult, school, bonusCritChance = ifCrit + shadowPowerCritIncrease, bonusHitChance = sfHit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Return VT mana
        val owner = sp.owner
        val vtdDebuff = owner?.sim?.target?.debuffs?.get(VampiricTouchDot.name)
        if(vtdDebuff != null){
            owner.addResource((result.first * 0.05).toInt(), Resource.Type.MANA, VampiricTouchDot.manaRestoreName)
        }

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
