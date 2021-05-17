package character.classes.mage.pet.abilities

import character.Ability
import character.classes.mage.talents.SpellPower
import character.classes.mage.talents.WintersChill
import data.Constants
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class Waterbolt : Ability() {
    companion object {
        const val name = "Waterbolt"
    }
    override val id: Int = 31707
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 1500

    val baseCastTimeMs = 2500
    override fun castTimeMs(sp: SimParticipant): Int = baseCastTimeMs
    override fun resourceCost(sp: SimParticipant): Double {
        return sp.character.klass.baseMana * 0.01
    }

    val baseDamage = Pair(486.0, 588.0)
    val school = Constants.DamageType.FROST
    val spellPowerCoeff = Spell.spellPowerCoeff(baseCastTimeMs)
    override fun cast(sp: SimParticipant) {
        val wintersChill = sp.sim.target.debuffs[WintersChill.name]
        val wintersChillCrit = (wintersChill?.state(sp)?.currentStacks ?: 0) * 0.02

        val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, school, spellPowerCoeff)
        val result = Spell.attackRoll(sp, damageRoll, school, bonusCritChance = wintersChillCrit)

         val event = Event(
            eventType = Event.Type.DAMAGE,
            damageType = school,
            abilityName = name,
            amount = result.first,
            result = result.second,
        )
        sp.logEvent(event)

        // Skipping procs since the WE has no abilities that would proc from a Waterbolt hit
    }
}
