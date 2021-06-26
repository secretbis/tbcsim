package character.races.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.priest.Priest
import character.classes.priest.talents.*
import character.races.Undead
import character.races.debuffs.DevouringPlagueDot
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class DevouringPlague : Ability() {
    override val id: Int = 19280
    override val name: String = "Devouring Plague"

    override fun cooldownMs(sp: SimParticipant): Int = 300000
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.race is Undead && sp.character.klass is Priest && super.available(sp)
    }

    val baseResourceCost = 985.0
    override fun resourceCost(sp: SimParticipant): Double {
        val mentalAgility = sp.character.klass.talents[MentalAgility.name] as MentalAgility?
        val mentalAgilityManaCostMultiplier = mentalAgility?.instantSpellManaCostReductionMultiplier() ?: 1.0

        return baseResourceCost * mentalAgilityManaCostMultiplier
    }

    override fun cast(sp: SimParticipant) {
        val school = Constants.DamageType.SHADOW

        var result = Spell.attackRoll(sp, 0.0, school, true, 0.0, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Apply the DoT
        if(result.second != EventResult.RESIST){
            sp.sim.target.addDebuff(DevouringPlagueDot(sp))
        }        
    }    
}

