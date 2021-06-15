package character.classes.priest.abilities

import character.Ability
import character.Buff
import character.Proc
import character.classes.priest.buffs.PowerInfusion
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.InnerFocus as InnerFocusTalent
import character.classes.priest.talents.SurgeOfLight as SurgeOfLightTalent
import character.classes.priest.talents.FocusedPower
import character.classes.priest.talents.HolySpecialization
import character.classes.priest.talents.DivineFury
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Smite : Ability() {
    companion object {
        const val name: String = "Smite"
    }
    override val id: Int = 585
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 2500
    override fun castTimeMs(sp: SimParticipant): Int {
        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val divineFury: DivineFury? = sp.character.klass.talentInstance(DivineFury.name)
        val solProc = sp.buffs[SurgeOfLightTalent.buffName] as SurgeOfLightTalent?
        if (solProc != null){
            return 1500
        }
        return ((baseCastTimeMs - (divineFury?.smiteHolyFireCastTimeReductionMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()   
    }

    val baseResourceCost = 385.0
    override fun resourceCost(sp: SimParticipant): Double {
        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0
        
        return baseResourceCost * piMult
    }

    val baseDamage = Pair(549.0, 616.0)
    val school = Constants.DamageType.HOLY
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val focusedPower: FocusedPower? = sp.character.klass.talentInstance(FocusedPower.name)
        val fpHit = focusedPower?.bonusSmiteMindBlastHitPct() ?: 0.0

        val hs: HolySpecialization? = sp.character.klass.talentInstance(HolySpecialization.name)
        val hsCrit = hs?.holySpellsCrit() ?: 0.0

        val innerFocus: InnerFocusTalent? = sp.character.klass.talentInstance(InnerFocusTalent.name)
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        val ifCrit = innerFocusBuff?.critPct() ?: 0.0      

        val solProc = sp.buffs[SurgeOfLightTalent.buffName] as SurgeOfLightTalent?
        val solCritModifier = solProc?.critChanceModifier() ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = hsCrit + ifCrit - solCritModifier, bonusHitChance = fpHit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Fire procs
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.HOLY_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.HOLY_DAMAGE_NON_PERIODIC)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
