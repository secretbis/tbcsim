package character.classes.pet.abilities

import character.Ability
import character.Resource
import sim.SimParticipant

class ClawRank9 : Ability() {
    override val id: Int
        get() = TODO("Not yet implemented")
    override val name: String = "Claw (Rank 9)"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.FOCUS
    override fun resourceCost(sp: SimParticipant): Double = 25.0

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
