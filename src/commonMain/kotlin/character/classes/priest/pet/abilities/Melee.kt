package character.classes.priest.pet.abilities

import mu.KotlinLogging

import character.Ability
import character.Resource
import character.classes.priest.Priest
import data.Constants
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant

class Melee : Ability() {
    val logger = KotlinLogging.logger {}

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

    val baseDamage = Pair(1.0, 1.0)
    val school = Constants.DamageType.SHADOW
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        //val ownerSpellDamage = sp.owner?.spellDamage() ?: 0
        val result = Spell.attackRoll(sp, damageRoll * (sp.spellDamage() / 4), school)

        logger.debug{ "Result ${result.first}" }

        sp.owner?.addResource((result.first).toInt(), Resource.Type.MANA, name)
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
