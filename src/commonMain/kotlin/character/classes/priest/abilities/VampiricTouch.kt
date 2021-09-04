package character.classes.priest.abilities

import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.Ability
import character.Proc
import character.classes.priest.*
import character.classes.priest.debuffs.VampiricTouchDot
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class VampiricTouch : Ability() {
    companion object {
        const val name = "Vampiric Touch"
    }

    override val id: Int = 34917
    override val name: String = Companion.name

    val school = Constants.DamageType.SHADOW
    var baseDamage = 650.0
    val baseDotTickCount = 5
    val baseDotDurationMs = 15000
    var dotDmgPerTick = 130
    val spellPowerCoeff = Spell.spellPowerCoeff(0, baseDotDurationMs)

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseResourceCost = 425.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?

        if (innerFocusBuff != null) {
            return 0.0
        }

        return baseResourceCost
    }

    override fun castTimeMs(sp: SimParticipant): Int = 1500

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
        
        // Apply the DoT
        if(result.second == EventResult.RESIST){
            sp.fireProc(listOf(Proc.Trigger.SPELL_RESIST), listOf(), this, event)
            return
        }

        sp.fireProc(listOf(Proc.Trigger.SPELL_HIT), listOf(), this, event)

        sp.sim.target.addDebuff(VampiricTouchDot(sp, result.first, tickCount))
    }
}
