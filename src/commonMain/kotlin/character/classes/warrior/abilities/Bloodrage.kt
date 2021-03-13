package character.classes.warrior.abilities

import character.Ability
import character.Resource
import sim.SimParticipant

class Bloodrage : Ability() {
    companion object {
        const val name: String = "Bloodrage"
    }

    override val id: Int = 2687
    override val name: String = Companion.name

    override fun cooldownMs(sp: SimParticipant): Int = 60000
    override fun gcdMs(sp: SimParticipant): Int = 0

    override fun cast(sp: SimParticipant) {
        // Close enough
        sp.addResource(20, Resource.Type.RAGE)
    }
}
