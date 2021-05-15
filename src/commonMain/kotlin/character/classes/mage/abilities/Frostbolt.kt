package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class Frostbolt : Ability() {
    companion object {
        const val name: String = "Frostbolt"
    }
    override val id: Int = 38697
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()

    val baseCastTimeMs = 3000
    override fun castTimeMs(sp: SimParticipant): Int {
        return super.castTimeMs(sp)
    }

    val baseResourceCost = 345.0
    override fun resourceCost(sp: SimParticipant): Double {
        return super.resourceCost(sp)
    }

    val baseDamage = Pair(630, 680)
    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
