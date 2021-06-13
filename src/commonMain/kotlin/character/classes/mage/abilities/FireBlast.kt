package character.classes.mage.abilities

import character.Ability
import character.Proc
import character.classes.mage.buffs.ArcanePower
import character.classes.mage.talents.*
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class FireBlast : Ability() {
    companion object {
        const val name: String = "Fire Blast"
    }
    override val id: Int = 30451
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCooldownMs = 8000
    override fun cooldownMs(sp: SimParticipant): Int {
        val impFb: ImprovedFireBlast? = sp.character.klass.talentInstance(ImprovedFireBlast.name)
        val impFbCooldownReductionMs = impFb?.fireBlastCooldownReductionMs() ?: 0

        return baseCooldownMs - impFbCooldownReductionMs
    }
    override fun castTimeMs(sp: SimParticipant): Int = 0

    val baseResourceCost = 465.0
    override fun resourceCost(sp: SimParticipant): Double {
        val apBuff = sp.buffs[ArcanePower.name] as ArcanePower?
        val apMult = apBuff?.manaCostMultiplier() ?: 1.0

        val pyromaniac: Pyromaniac? = sp.character.klass.talentInstance(Pyromaniac.name)
        val pyroMult = 1.0 - (pyromaniac?.fireSpellManaCostReduction() ?: 0.0)

        return baseResourceCost * apMult * pyroMult
    }

    val baseDamage = Pair(664.0, 786.0)
    val school = Constants.DamageType.FIRE
    val spellPowerCoeff = Spell.spellPowerCoeff(0)
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
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Fire procs
        val triggerTypes = when(result.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE_NON_PERIODIC)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}
