package character.races.abilities

import character.Ability
import character.Buff
import character.Stats
import character.classes.priest.Priest
import character.races.NightElf
import character.races.debuffs.StarshardsDot
import data.Constants
import mechanics.General
import mechanics.Spell
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

class Starshards : Ability() {
    override val id: Int = 19305
    override val name: String = "Starshards"

    override fun cooldownMs(sp: SimParticipant): Int = 30000
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.race is NightElf && sp.character.klass is Priest && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        val school = Constants.DamageType.ARCANE

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
            sp.sim.target.addDebuff(StarshardsDot(sp))
        }        
    }    
}

