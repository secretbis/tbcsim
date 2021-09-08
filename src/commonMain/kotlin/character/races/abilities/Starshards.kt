package character.races.abilities

import character.Ability
import character.Buff
import character.Proc
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
    override val id: Int = 25446
    override val name: String = "Starshards"

    val school = Constants.DamageType.ARCANE

    override fun cooldownMs(sp: SimParticipant): Int = 30000

    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun available(sp: SimParticipant): Boolean {
        return sp.character.race is NightElf && sp.character.klass is Priest && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        val result = Spell.attackRoll(sp, 0.0, school, isBinary = true, canCrit = false)

        val event = Event(
            eventType = EventType.DAMAGE,
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

        sp.sim.target.addDebuff(StarshardsDot(sp))
    }     
}

