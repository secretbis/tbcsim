package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class Fireball : Ability() {
    companion object {
        const val name: String = "Fireball"
    }
    override val id: Int = 38692
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3500
    override fun castTimeMs(sp: SimParticipant): Int {
        return super.castTimeMs(sp)
    }

    val baseResourceCost = 465.0
    override fun resourceCost(sp: SimParticipant): Double {
        return super.resourceCost(sp)
    }

    val baseDamage = Pair(717, 913)
    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
