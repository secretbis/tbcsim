package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.talents.*
import data.Constants
import data.itemsets.SkyshatterHarness
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class EarthShock : Ability() {
    companion object {
        const val name = "Earth Shock"
    }

    override val id: Int = 25454
    override val name: String = Companion.name

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int {
        val reverberation = sp.character.klass.talents[Reverberation.name] as Reverberation?
        return 6000 - (reverberation?.shockCooldownReductionAmountMs() ?: 0).toInt()
    }
    override val sharedCooldown: SharedCooldown = SharedCooldown.SHAMAN_SHOCK

    override fun resourceCost(sp: SimParticipant): Double {
        val convection = sp.character.klass.talents[Convection.name] as Convection?
        val cvRed = convection?.lightningAndShockCostReduction() ?: 0.0

        val mq = sp.character.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val shFocus = sp.buffs[ShamanisticFocus.name]
        val shfRed = if(shFocus != null) { 0.60 } else 0.0

        val eleFocus = sp.buffs[ElementalFocus.name]
        val elefRed = if(eleFocus != null) { 0.40 } else 0.0

        // Check T6 set bonus
        val t6Bonus = sp.buffs[SkyshatterHarness.TWO_SET_BUFF_NAME] != null
        val t6Discount = if(t6Bonus) { SkyshatterHarness.twoSetShockCostReductionPct() } else 0.0

        return General.resourceCostReduction(535.0, listOf(cvRed, mqRed, shfRed, elefRed, t6Discount))
    }


    val baseDamage = Pair(658.0, 693.0)
    override fun cast(sp: SimParticipant) {
        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.NATURE

        val concussion = sp.character.klass.talents[Concussion.name] as Concussion?
        val concussionMod = concussion?.shockAndLightningMultiplier() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff) * concussionMod
        val result = Spell.attackRoll(sp, damageRoll, school)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Proc anything that can proc off Nature damage
        val baseTriggerTypes = listOf(Proc.Trigger.SHAMAN_CAST_SHOCK)
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.NATURE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.NATURE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sp.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }
}
