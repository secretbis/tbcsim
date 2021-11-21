package character.classes.mage.abilities

import character.Ability
import character.Proc
import character.classes.mage.buffs.ArcanePower
import character.classes.mage.buffs.PresenceOfMind
import character.classes.mage.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Frostbolt : Ability() {
    companion object {
        const val name: String = "Frostbolt"
    }
    override val id: Int = 38697
    override val name: String = Companion.name
    override val icon: String = "spell_frost_frostbolt02.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3000
    override fun castTimeMs(sp: SimParticipant): Int {
        val pomBuff = sp.buffs[PresenceOfMind.name] as PresenceOfMind?
        return if(pomBuff != null) {
            sp.consumeBuff(pomBuff)
            0
        } else {
            val impFb: ImprovedFrostbolt? = sp.character.klass.talentInstance(ImprovedFrostbolt.name)
            return ((baseCastTimeMs - (impFb?.frostboltCastTimeReductionMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()
        }
    }

    val baseResourceCost = 345.0
    override fun resourceCost(sp: SimParticipant): Double {
        val apBuff = sp.buffs[ArcanePower.name] as ArcanePower?
        val apMult = apBuff?.manaCostMultiplier() ?: 1.0

        val frostChanneling: FrostChanneling? = sp.character.klass.talentInstance(FrostChanneling.name)
        val fcMult = frostChanneling?.frostSpellManaCostReductionMultiplier() ?: 1.0

        return baseResourceCost * apMult * fcMult
    }

    val baseDamage = Pair(630.0, 680.0)
    val school = Constants.DamageType.FROST
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val elementalPrecision: ElementalPrecision? = sp.character.klass.talentInstance(ElementalPrecision.name)
        val emHit = elementalPrecision?.bonusFireFrostHitPct() ?: 0.0

        val empFb: EmpoweredFrostbolt? = sp.character.klass.talentInstance(EmpoweredFrostbolt.name)
        val bonusFbCrit = empFb?.frostboltAddlCritPct() ?: 0.0
        val bonusFbSpellDmg = empFb?.frostboltBonusSpellDamageMultiplier() ?: 1.0

        val wintersChill = sp.sim.target.debuffs[WintersChill.name]
        val wintersChillCrit = (wintersChill?.state(sp.sim.target)?.currentStacks ?: 0) * 0.02

        val piercingIce: PiercingIce? = sp.character.klass.talentInstance(PiercingIce.name)
        val piercingIceMult = piercingIce?.frostDamageMultiplier() ?: 1.0

        val iceShards: IceShards? = sp.character.klass.talentInstance(IceShards.name)
        val iceShardsCritBonusMult = iceShards?.frostCritDamageBonusMult() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff, bonusSpellDamageMultiplier = bonusFbSpellDmg) * piercingIceMult
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = bonusFbCrit + wintersChillCrit, bonusHitChance = emHit, bonusCritMultiplier = iceShardsCritBonusMult)

         val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Frost procs
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FROST_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FROST_DAMAGE)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FROST_DAMAGE)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FROST_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
