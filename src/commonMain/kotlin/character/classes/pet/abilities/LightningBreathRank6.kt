package character.classes.pet.abilities

import character.Ability
import character.Resource
import sim.SimParticipant

class LightningBreathRank6 : Ability() {
    override val id: Int = 25012
    // TODO: Is there no Outland rank of this?
    override val name: String = "Lightning Breath (Rank 6)"
    override fun gcdMs(sp: SimParticipant): Int = sp.physicalGcd().toInt()

    override fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.FOCUS
    override fun resourceCost(sp: SimParticipant): Double = 50.0

    override fun cast(sp: SimParticipant) {

    }
}
