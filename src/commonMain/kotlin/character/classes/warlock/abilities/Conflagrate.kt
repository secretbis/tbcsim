package character.classes.warlock.abilities

import character.Ability
import character.Proc
import character.classes.warlock.debuffs.ImmolateDot
import character.classes.warlock.talents.Cataclysm
import character.classes.warlock.talents.Conflagrate
import character.classes.warlock.talents.Devastation
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Conflagrate : Ability() {
    companion object {
        const val name = "Conflagrate"
    }

    override val id: Int = 32231
    override val name: String = Companion.name
    override val icon: String = "spell_fire_fireball.jpg"
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun cooldownMs(sp: SimParticipant): Int = 10000

    override fun available(sp: SimParticipant): Boolean {
        return sp.sim.target.debuffs[ImmolateDot.name] != null && (sp.character.klass.talents[Conflagrate.name]?.currentRank ?: 0) > 0
    }

    override fun resourceCost(sp: SimParticipant): Double {
        val cataclysm = sp.character.klass.talents[Cataclysm.name] as Cataclysm?
        val cataRed = cataclysm?.destructionCostReduction() ?: 0.0

        return General.resourceCostReduction(305.0, listOf(cataRed))
    }

    val baseDamage = Pair(579.0, 722.0)
    override fun cast(sp: SimParticipant) {
        val devastation = sp.character.klass.talents[Devastation.name] as Devastation?
        val devastationAddlCrit = devastation?.additionalDestructionCritChance() ?: 0.0

        val spellPowerCoeff = Spell.spellPowerCoeff(0)
        val school = Constants.DamageType.FIRE

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, isBinary = false, devastationAddlCrit)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            ability = this,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Remove the Immolate DoT
        sp.consumeDebuff(ImmolateDot(sp))

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
