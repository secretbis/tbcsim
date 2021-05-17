package character.classes.mage.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.mage.buffs.ArcanePower
import character.classes.mage.talents.CriticalMass
import character.classes.mage.talents.ElementalPrecision
import character.classes.mage.talents.Incineration
import character.classes.mage.talents.Pyromaniac
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class Scorch : Ability() {
    companion object {
        const val name: String = "Scorch"
    }
    override val id: Int = 27074
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 1500
    override fun castTimeMs(sp: SimParticipant): Int = baseCastTimeMs

    val baseResourceCost = 180.0
    override fun resourceCost(sp: SimParticipant): Double {
        val apBuff = sp.buffs[ArcanePower.name] as ArcanePower?
        val apMult = apBuff?.manaCostMultiplier() ?: 1.0

        val pyromaniac: Pyromaniac? = sp.character.klass.talentInstance(Pyromaniac.name)
        val pyroMult = 1.0 - (pyromaniac?.fireSpellManaCostReduction() ?: 0.0)

        return baseResourceCost * apMult * pyroMult
    }

    val buff = object : Buff() {
        override val name: String = "Improved Scorch"
        override val durationMs: Int = 30000
        override val maxStacks: Int = 5

        override fun modifyStats(sp: SimParticipant): Stats {
            val state = state(sp)
            return Stats(fireDamageMultiplier = 1.0 + (0.03 * state.currentStacks))
        }
    }

    val baseDamage = Pair(305.0, 361.0)
    val school = Constants.DamageType.FIRE
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val criticalMass: CriticalMass? = sp.character.klass.talentInstance(CriticalMass.name)
        val cmCrit = criticalMass?.fireSpellAddlCritPct() ?: 0.0

        val elementalPrecision: ElementalPrecision? = sp.character.klass.talentInstance(ElementalPrecision.name)
        val emHit = elementalPrecision?.bonusFireFrostHitPct() ?: 0.0

        val pyromaniac: Pyromaniac? = sp.character.klass.talentInstance(Pyromaniac.name)
        val pyroCrit = pyromaniac?.fireSpellAddlCritPct() ?: 0.0

        val combustion = sp.buffs[Combustion.name] as? character.classes.mage.buffs.Combustion?
        val combustionCrit = combustion?.getFireSpellAddlCritPct(sp) ?: 0.0

        val incineration: Incineration? = sp.character.klass.talentInstance(Incineration.name)
        val incCrit = incineration?.fireBlastScorchAddlCritPct() ?: 0.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = cmCrit + pyroCrit + combustionCrit + incCrit, bonusHitChance = emHit)

         val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        if(result.second != Event.Result.RESIST) {
            sp.sim.addRaidBuff(buff)
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
