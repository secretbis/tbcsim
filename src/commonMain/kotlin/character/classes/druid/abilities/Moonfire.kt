package character.classes.druid.abilities

import character.Ability
import sim.SimParticipant

/**
 *
 */
class Moonfire : Ability() {
    companion object {
        const val name = "Moonfire"
    }

    // TODO: lookup this actual value
    override val id: Int = 100
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    override fun resourceCost(sp: SimParticipant): Double {
        return super.resourceCost(sp)
    }

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}