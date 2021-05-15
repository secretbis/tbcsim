package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class Scorch : Ability() {
    companion object {
        const val name: String = "Scorch"
    }
    override val id: Int = 27074
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3500
    override fun castTimeMs(sp: SimParticipant): Int {
        return super.castTimeMs(sp)
    }

    val baseResourceCost = 180.0
    override fun resourceCost(sp: SimParticipant): Double {
        return super.resourceCost(sp)
    }

    val baseDamage = Pair(305, 361)
    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
