package character.classes.priest.abilities

import character.Ability
import character.Proc
import character.classes.priest.buffs.PowerInfusion
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.talents.InnerFocus as InnerFocusTalent
import character.classes.priest.talents.SurgeOfLight as SurgeOfLightTalent
import character.classes.priest.talents.FocusedPower
import character.classes.priest.talents.SearingLight
import character.classes.priest.talents.HolySpecialization
import character.classes.priest.talents.DivineFury
import character.classes.priest.debuffs.HolyFireDot
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class HolyFire : Ability() {
    companion object {
        const val name: String = "Holy Fire"
    }
    override val id: Int = 38692
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3500
    override fun castTimeMs(sp: SimParticipant): Int {
        val divineFury: DivineFury? = sp.character.klass.talentInstance(DivineFury.name)
        return ((baseCastTimeMs - (divineFury?.smiteHolyFireCastTimeReductionMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()
    }

    val baseResourceCost = 290.0
    val school = Constants.DamageType.HOLY
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun resourceCost(sp: SimParticipant): Double {
        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0

        return baseResourceCost * piMult
    }

    val baseDamage = Pair(426.0, 537.0)
    override fun cast(sp: SimParticipant) {
        val hs: HolySpecialization? = sp.character.klass.talentInstance(HolySpecialization.name)
        val hsCrit = hs?.holySpellsCrit() ?: 0.0

        val innerFocus: InnerFocusTalent? = sp.character.klass.talentInstance(InnerFocusTalent.name)
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        val ifCrit = innerFocusBuff?.critPct() ?: 0.0      

        val searingLight: SearingLight? = sp.character.klass.talentInstance(SearingLight.name)
        val searingLightMultiplier = searingLight?.smiteHolyFireDamageMultiplier() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll * searingLightMultiplier, school, bonusCritChance = hsCrit + ifCrit)

         val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != EventResult.RESIST) {
            sp.sim.target.addDebuff(HolyFireDot(sp))
        }

        // Fire procs
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.HOLY_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.HOLY_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
