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

class BladeFlurry : Ability() {
    companion object {
        const val name = "Blade Flurry"
    }

    override val id: Int = 13877
    override val name: String = Companion.name

    override fun cooldownMs(sp: SimParticipant): Int = 120000
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 25.0

    override fun available(sp: SimParticipant): Boolean {
        val bf = sp.character.klass.talents[character.classes.rogue.talents.BladeFlurry.name] as character.classes.rogue.talents.BladeFlurry?
        val available = if(bf != null){ bf.currentRank == bf.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having the corresponding talent" }
        }

        return available && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(character.classes.rogue.buffs.BladeFlurry())
    }
}
