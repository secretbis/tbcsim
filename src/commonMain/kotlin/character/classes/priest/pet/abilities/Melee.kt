package character.classes.priest.pet.abilities

import character.Ability
import character.Resource
import character.classes.priest.Priest
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant

class Melee : Ability() {
    companion object {
        const val name = "Melee"
    }
    override val id: Int = 1
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 1500

    val baseCastTimeMs = 2500
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun resourceCost(sp: SimParticipant): Double {
        return sp.character.klass.baseMana * 0.01
    }

    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val result = Spell.attackRoll(sp, sp.spellDamage().toDouble(), school)

        sp.owner?.addResource((result.first).toInt() * 3, Resource.Type.MANA, name)
        val event = Event(
            eventType = EventType.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)
    }
}
