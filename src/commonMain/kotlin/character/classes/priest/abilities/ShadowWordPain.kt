package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.Ability
import character.Proc
import character.classes.priest.*
import character.classes.priest.debuffs.ShadowWordPainDot
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class ShadowWordPain : Ability() {
    companion object {
        const val name = "Shadow Word Pain"
    }

    override val id: Int = 25368
    override val name: String = Companion.name

    val school = Constants.DamageType.SHADOW
    var baseDamage = 1236.0
    val baseDotTickCount = 6
    val baseDotDurationMs = 18000
    var dotDmgPerTick = 206.0
    // See https://www.warcrafttavern.com/tbc/guides/shadow-priest-damage-coefficients/
    val spellPowerCoeff = 1.1

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseResourceCost = 575.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        return baseResourceCost
    }

    override fun cast(sp: SimParticipant) {
        // snapshot damage on initial cast
        val damageRoll = Spell.baseDamageRollSingle(sp, baseDamage, school, spellPowerCoeff)
        var result = Spell.attackRoll(sp, damageRoll, school, isBinary = true, canCrit = false)
        var tickCount = baseDotTickCount;

        val event = Event(
            eventType = EventType.SPELL_CAST,
            damageType = school,
            abilityName = name,
            result = result.second,
        )
        sp.logEvent(event)
        
        if(result.second == EventResult.RESIST){
            sp.fireProc(listOf(Proc.Trigger.SPELL_RESIST), listOf(), this, event)
            return
        }

        sp.fireProc(listOf(Proc.Trigger.SPELL_HIT), listOf(), this, event)

        sp.sim.target.addDebuff(ShadowWordPainDot(sp, result.first, tickCount))
    }
}
