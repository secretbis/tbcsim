package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class Combustion : Ability() {
    companion object {
        const val name: String = "Combustion"
    }
    override val id: Int = 11129
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
