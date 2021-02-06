package character.classes.shaman.abilities

import character.Ability
import character.Proc
import character.classes.shaman.debuffs.FlameShockDot
import character.classes.shaman.talents.*
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.SimIteration

class FlameShock : Ability() {
    companion object {
        const val name = "Flame Shock"
    }

    override val id: Int = 25457
    override val name: String = Companion.name

    override fun cooldownMs(sim: SimIteration): Int {
        val reverberation = sim.subject.klass.talents[Reverberation.name] as Reverberation?
        return 6000 - (reverberation?.shockCooldownReductionAmountMs() ?: 0).toInt()
    }
    override val sharedCooldown: SharedCooldown = SharedCooldown.SHAMAN_SHOCK
    override fun gcdMs(sim: SimIteration): Int = sim.spellGcd().toInt()

    override fun resourceCost(sim: SimIteration): Double {
        val convection = sim.subject.klass.talents[Convection.name] as Convection?
        val cvRed = convection?.lightningAndShockCostReduction() ?: 0.0

        val mq = sim.subject.klass.talents[MentalQuickness.name] as MentalQuickness?
        val mqRed = mq?.instantManaCostReduction() ?: 0.0

        val shFocus = sim.buffs.find { it.name == ShamanisticFocus.name }
        val shfRed = if(shFocus != null) { 0.60 } else 0.0

        val eleFocus = sim.buffs.find { it.name == ElementalFocus.name }
        val elefRed = if(eleFocus != null) { 0.40 } else 0.0

        return General.resourceCostReduction(500.0, listOf(cvRed, mqRed, shfRed, elefRed))
    }

    val baseDamage = 377.0
    override fun cast(sim: SimIteration) {
        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.FIRE

        val concussion = sim.subject.klass.talents[Concussion.name] as Concussion?
        val concussionMod = concussion?.shockAndLightningMultiplier() ?: 1.0

        val damageRoll = Spell.baseDamageRoll(sim, baseDamage, spellPowerCoeff, school) * concussionMod
        val result = Spell.attackRoll(sim, damageRoll, school)

        val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sim.logEvent(event)

        // Apply the DoT
        sim.addDebuff(FlameShockDot())

        // Proc anything that can proc off Fire damage
        val baseTriggerTypes = listOf(Proc.Trigger.SHAMAN_CAST_SHOCK)
        val triggerTypes = when(result.second) {
            Event.Result.HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.RESIST -> listOf(Proc.Trigger.SPELL_RESIST)
            Event.Result.PARTIAL_RESIST_HIT -> listOf(Proc.Trigger.SPELL_HIT, Proc.Trigger.FIRE_DAMAGE)
            Event.Result.PARTIAL_RESIST_CRIT -> listOf(Proc.Trigger.SPELL_CRIT, Proc.Trigger.FIRE_DAMAGE)
            else -> null
        }

        if(triggerTypes != null) {
            sim.fireProc(baseTriggerTypes + triggerTypes, listOf(), this, event)
        }
    }
}
