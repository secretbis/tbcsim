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

class MindFlay3 : Ability() {
    companion object {
        const val name: String = "Mind Flay (3 ticks)"
    }
    override val id: Int = 25387
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 2000
    override fun castTimeMs(sp: SimParticipant): Int {
        return (baseCastTimeMs / sp.spellHasteMultiplier()).toInt()   
    }

    val baseResourceCost = 230.0
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

    val dmgPerTick = 172.0
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = 0.19 // Per tick
    override fun cast(sp: SimParticipant) {
        val shadowFocus: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = shadowFocus?.shadowHitIncreasePct() ?: 0.0 

        val damageRoll = Spell.baseDamageRollSingle(sp, dmgPerTick, school, spellPowerCoeff)
        val damageRoll2 = Spell.baseDamageRollSingle(sp, dmgPerTick, school, spellPowerCoeff)
        val damageRoll3 = Spell.baseDamageRollSingle(sp, dmgPerTick, school, spellPowerCoeff)

        val shadowWeaving = sp.sim.target.debuffs.get(ShadowWeaving.name) as ShadowWeavingDebuff?
        val swMult = shadowWeaving?.shadowDamageMultiplierPct() ?: 1.0

        val result = Spell.attackRoll(sp, (damageRoll + damageRoll2 + damageRoll3) * swMult, school, bonusHitChance = sfHit, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
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
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.SHADOW_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
