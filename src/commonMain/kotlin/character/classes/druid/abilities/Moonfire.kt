package character.classes.druid.abilities

import character.Ability
import character.Proc
import character.classes.druid.debuff.MoonfireDot
import character.classes.druid.talents.*
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

/**
 *
 */
class Moonfire : Ability() {
    companion object {
        const val name = "Moonfire"
    }

    // TODO: lookup this actual value
    override val id: Int = 100
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCost = 495.0
    override fun resourceCost(sp: SimParticipant): Double {
        val moonglow = sp.getTalent<Moonglow>(Moonglow.name)
        val costReductionPercent = moonglow?.reducedManaCostPercent() ?: 0.0
        return General.resourceCostReduction(baseCost, listOf(costReductionPercent))
    }

    val baseDamage = Pair(305.0, 357.0)
    val baseDot = 600.0
    val school = Constants.DamageType.ARCANE
    override fun cast(sp: SimParticipant) {
        val impMoonfire = sp.getTalent<ImprovedMoonfire>(ImprovedMoonfire.name)
        val impMoonfireIncreasedCritChance = impMoonfire?.increasedMoonfireCritChancePercent() ?: 0.0
        val impMoonfireIncreasedDamagePercent = impMoonfire?.increasedMoonfireDamagePercent() ?: 0.0

        val vengeance = sp.getTalent<Vengeance>(Vengeance.name)
        val increasedCritBonusDamage = vengeance?.increasedCritBonusDamagePercent() ?: 0.0

        val moonfury = sp.getTalent<Moonfury>(Moonfury.name)
        val moonfuryBonusDamagePercent = moonfury?.increasedDamagePercent() ?: 0.0

        val instantSpellPowerCoeff = 0.1495

        val damageMulti = 1.0 + impMoonfireIncreasedDamagePercent + moonfuryBonusDamagePercent

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, instantSpellPowerCoeff) * damageMulti
        val damageResult = Spell.attackRoll(
            sp,
            damageRoll,
            school,
            isBinary = false,
            bonusCritChance = impMoonfireIncreasedCritChance,
            bonusCritMultiplier = 1.0 + increasedCritBonusDamage
        )

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = damageResult.first,
            result = damageResult.second
        )
        sp.logEvent(event)

        sp.sim.target.addDebuff(MoonfireDot(sp))

        // Proc anything that can proc off non-periodic spell damage
        val triggerTypes = when(damageResult.second) {
            EventResult.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.ARCANE_DAMAGE)
            EventResult.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.ARCANE_DAMAGE)
            EventResult.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            EventResult.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.ARCANE_DAMAGE)
            EventResult.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.ARCANE_DAMAGE)
            else -> null
        }

        if (triggerTypes != null) {
            sp.fireProc(triggerTypes, listOf(), this, event)
        }
    }
}