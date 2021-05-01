package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*
import mechanics.Rating
import mechanics.Spell
import mu.KotlinLogging

class ColdBlood : Ability() {
    companion object {
        const val name = "Cold Blood"
    }

    override val id: Int = 14177
    override val name: String = Companion.name

    override fun cooldownMs(sp: SimParticipant): Int = 180000
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    override fun available(sp: SimParticipant): Boolean {
        val cb = sp.character.klass.talents[character.classes.rogue.talents.ColdBlood.name] as character.classes.rogue.talents.ColdBlood?
        val available = if(cb != null){ cb.currentRank == cb.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.warn{ "Tried to use ability $name without having the corresponding talent" }
        }

        return available && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(character.classes.rogue.buffs.ColdBlood())
    }
}
