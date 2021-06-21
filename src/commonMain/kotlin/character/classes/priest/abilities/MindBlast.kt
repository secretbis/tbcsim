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

class MindBlast : Ability() {
    companion object {
        const val name: String = "Mind Blast"
    }
    override val id: Int = 25375
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCooldownMs = 8000
    override fun cooldownMs(sp: SimParticipant): Int {
        val impMb: ImprovedMindBlast? = sp.character.klass.talentInstance(ImprovedMindBlast.name)
        val impMbCooldownReductionMs = impMb?.mindBlastCooldownReductionMs() ?: 0

        return baseCooldownMs - impMbCooldownReductionMs
    }

    val baseCastTimeMs = 1500
    override fun castTimeMs(sp: SimParticipant): Int {
        return (baseCastTimeMs / sp.spellHasteMultiplier()).toInt()   
    }

    val baseResourceCost = 450.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        if(innerFocusBuff != null){
            return 0.0
        }

        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0

        val fm: FocusedMind? = sp.character.klass.talentInstance(FocusedMind.name)
        val fmMult = fm?.mindBlastFlayManaCostReductionMultiplier() ?: 1.0
        
        return baseResourceCost * fmMult * piMult
    }

    val baseDamage = Pair(711.0, 752.0)
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val focusedPower: FocusedPower? = sp.character.klass.talentInstance(FocusedPower.name)
        val fpHit = focusedPower?.bonusSmiteMindBlastHitPct() ?: 0.0

        val shadowFocus: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = shadowFocus?.shadowHitIncreasePct() ?: 0.0

        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        val ifCrit = innerFocusBuff?.critPct() ?: 0.0      

        val shadowPower = sp.character.klass.talents[ShadowPower.name] as ShadowPower?
        val shadowPowerCritIncrease = shadowPower?.mindBlastSwDCritIncreasePct() ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)

        val shadowWeaving = sp.sim.target.debuffs.get(ShadowWeaving.name) as ShadowWeavingDebuff?
        val swMult = shadowWeaving?.shadowDamageMultiplierPct() ?: 1.0

        val result = Spell.attackRoll(sp, damageRoll * swMult, school, bonusCritChance = ifCrit + shadowPowerCritIncrease, bonusHitChance = fpHit + sfHit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(innerFocusBuff != null){
            sp.consumeBuff(innerFocusBuff)
        }

        // Return VT mana
        val vtdDebuff = sp.sim.target.debuffs.get(VampiricTouchDot.name)
        if(vtdDebuff != null){
            sp.addResource((result.first * 0.05).toInt(), Resource.Type.MANA, VampiricTouchDot.manaRestoreName)
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
