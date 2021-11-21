package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.abilities.*
import mechanics.Rating
import mechanics.Spell
import mu.KotlinLogging

class Preparation : Ability() {
    companion object {
        const val name = "Preparation"
    }

    override val id: Int = 14185
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_antishadow.jpg"

    override fun cooldownMs(sp: SimParticipant): Int = 600000
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    override fun available(sp: SimParticipant): Boolean {
        val prep = sp.character.klass.talents[character.classes.rogue.talents.Preparation.name] as character.classes.rogue.talents.Preparation?
        val available = if(prep != null){ prep.currentRank == prep.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.warn{ "Tried to use ability $name without having the corresponding talent" }
        }

        return available && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.abilityState[Vanish.name]?.cooldownStartMs = -1
        sp.abilityState[ColdBlood.name]?.cooldownStartMs = -1
    }
}
