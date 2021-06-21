package character.classes.priest.abilities

import character.Ability
import character.Proc
import character.classes.priest.buffs.PowerInfusion
import character.classes.priest.buffs.InnerFocus as InnerFocusBuff
import character.classes.priest.debuffs.VampiricTouchDot
import character.classes.priest.talents.*
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

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseResourceCost = 425.0
    override fun resourceCost(sp: SimParticipant): Double {
        val innerFocusBuff = sp.buffs[InnerFocusBuff.name] as InnerFocusBuff?
        if(innerFocusBuff != null){
            sp.consumeBuff(innerFocusBuff)
            return 0.0
        }

        val piBuff = sp.buffs[PowerInfusion.name] as PowerInfusion?
        val piMult = piBuff?.manaCostMultiplier() ?: 1.0

        return baseResourceCost * piMult
    }

    val baseCastTimeMs = 1500
    override fun castTimeMs(sp: SimParticipant): Int {
        return (baseCastTimeMs / sp.spellHasteMultiplier()).toInt()
    }

    override fun cast(sp: SimParticipant) {
        val school = Constants.DamageType.SHADOW

        val shadowFocus: ShadowFocus? = sp.character.klass.talentInstance(ShadowFocus.name)
        val sfHit = shadowFocus?.shadowHitIncreasePct() ?: 0.0 

        var result = Spell.attackRoll(sp, 0.0, school, true, 0.0, bonusHitChance = sfHit, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Apply the DoT
        if(result.second != EventResult.MISS){
            sp.sim.target.addDebuff(VampiricTouchDot(sp))
        }        
    }
}
