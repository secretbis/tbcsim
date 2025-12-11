package character.classes.rogue.abilities

import character.*
import mechanics.Melee
import sim.Event
import sim.SimParticipant
import data.Constants
import data.model.Item
import character.classes.rogue.talents.*
import character.classes.rogue.buffs.*
import io.github.oshai.kotlinlogging.KotlinLogging

class AdrenalineRush : Ability() {
    companion object {
        const val name = "Adrenaline Rush"
    }

    override val id: Int = 13750
    override val name: String = Companion.name
    override val icon: String = "spell_shadow_shadowworddominate.jpg"

    override fun cooldownMs(sp: SimParticipant): Int = 300000
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val castableOnGcd: Boolean = true

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.ENERGY
    override fun resourceCost(sp: SimParticipant): Double = 0.0

    override fun available(sp: SimParticipant): Boolean {
        val ar = sp.character.klass.talents[character.classes.rogue.talents.AdrenalineRush.name] as character.classes.rogue.talents.AdrenalineRush?
        val available = if(ar != null){ ar.currentRank == ar.maxRank } else { false }
        if (!available) {
            KotlinLogging.logger{}.debug{ "Tried to use ability $name without having the corresponding talent" }
        }

        return available && super.available(sp)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(character.classes.rogue.buffs.AdrenalineRush())
    }
}
