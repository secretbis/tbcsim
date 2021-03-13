package character.classes.pet.abilities

import character.Ability
import character.Resource
import sim.SimParticipant

class GoreRank9 : Ability() {
    override val id: Int = 35298
    override val name: String = "Gore (Rank 9)"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.FOCUS
    override fun resourceCost(sp: SimParticipant): Double = 25.0

    override fun cast(sp: SimParticipant) {

    }
}
