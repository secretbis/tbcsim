package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.debuffs.ImmolateDot
import character.classes.warlock.talents.Bane
import character.classes.warlock.talents.Cataclysm
import character.classes.warlock.talents.Devastation
import character.classes.warlock.talents.ImprovedImmolate
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Immolate : Ability() {
    companion object {
        const val name = "Immolate"
    }
    override val id: Int = 32231
    override val name: String = Companion.name
    override val icon: String = "spell_fire_immolation.jpg"

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        val cataclysm = sp.character.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(445.0, listOf(cataRed))
    }

    val baseCastTimeMs = 2000
    override fun castTimeMs(sp: SimParticipant): Int {
        val bane = sp.character.klass.talents[Bane.name] as Bane?
        return ((baseCastTimeMs - (bane?.destructionCastReductionAmountMs() ?: 0)) / sp.spellHasteMultiplier()).toInt()
    }

    val baseDamage = 327.0
    override fun cast(sp: SimParticipant) {
        val devastation = sp.character.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val impImmolate = sp.character.klass.talents[ImprovedImmolate.name] as ImprovedImmolate?
        val impImmolateInitialMultiplier = impImmolate?.immolateInitialDamageMultiplier() ?: 1.0

        // Per lock discord, value is 20%
        val spellPowerCoeff = 0.2
        val school = Constants.DamageType.FIRE

        val damageRoll = Spell.baseDamageRollSingle(sp, baseDamage, school, spellPowerCoeff) * impImmolateInitialMultiplier
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Apply the DoT
        sp.sim.target.addDebuff(ImmolateDot(sp))

        // Proc anything that can proc off non-periodic Fire damage
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
