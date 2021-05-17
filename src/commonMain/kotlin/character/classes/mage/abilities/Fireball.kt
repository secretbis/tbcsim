package character.classes.mage.abilities

import character.Ability
import character.Proc
import character.classes.mage.buffs.ArcanePower
import character.classes.mage.buffs.PresenceOfMind
import character.classes.mage.debuffs.FireballDot
import character.classes.mage.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class Fireball : Ability() {
    companion object {
        const val name: String = "Fireball"
    }
    override val id: Int = 38692
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3500
    override fun castTimeMs(sp: SimParticipant): Int {
        val pomBuff = sp.buffs[PresenceOfMind.name] as PresenceOfMind?
        return if(pomBuff != null) {
            sp.consumeBuff(pomBuff)
            0
        } else {
            val impFb: ImprovedFireball? = sp.character.klass.talentInstance(ImprovedFireball.name)
            return baseCastTimeMs - (impFb?.fireballCastTimeReductionMs() ?: 0)
        }
    }

    val baseResourceCost = 465.0
    val school = Constants.DamageType.FIRE
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun resourceCost(sp: SimParticipant): Double {
        val apBuff = sp.buffs[ArcanePower.name] as ArcanePower?
        val apMult = apBuff?.manaCostMultiplier() ?: 1.0

        val pyromaniac: Pyromaniac? = sp.character.klass.talentInstance(Pyromaniac.name)
        val pyroMult = 1.0 - (pyromaniac?.fireSpellManaCostReduction() ?: 0.0)

        return baseResourceCost * apMult * pyroMult
    }

    val baseDamage = Pair(717.0, 913.0)
    override fun cast(sp: SimParticipant) {
        val criticalMass: CriticalMass? = sp.character.klass.talentInstance(CriticalMass.name)
        val cmCrit = criticalMass?.fireSpellAddlCritPct() ?: 0.0

        val elementalPrecision: ElementalPrecision? = sp.character.klass.talentInstance(ElementalPrecision.name)
        val emHit = elementalPrecision?.bonusFireFrostHitPct() ?: 0.0

        val empFb: EmpoweredFireball? = sp.character.klass.talentInstance(EmpoweredFireball.name)
        val bonusFbSpellDmg = empFb?.fireballAddlSpellDamageMultiplier() ?: 1.0

        val pyromaniac: Pyromaniac? = sp.character.klass.talentInstance(Pyromaniac.name)
        val pyroCrit = pyromaniac?.fireSpellAddlCritPct() ?: 0.0

        val combustion = sp.buffs[Combustion.name] as? character.classes.mage.buffs.Combustion?
        val combustionCrit = combustion?.getFireSpellAddlCritPct(sp) ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff, bonusSpellDamageMultiplier = bonusFbSpellDmg)
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = cmCrit + pyroCrit + combustionCrit, bonusHitChance = emHit)

         val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != Event.Result.RESIST) {
            sp.sim.target.addDebuff(FireballDot(sp))
        }

        // Fire procs
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
