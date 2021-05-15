package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class FireBlast : Ability() {
    companion object {
        const val name: String = "Fire Blast"
    }
    override val id: Int = 30451
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun cooldownMs(sp: SimParticipant): Int = 8000
    override fun castTimeMs(sp: SimParticipant): Int = 0

    val baseResourceCost = 465.0
    override fun resourceCost(sp: SimParticipant): Double {
        return super.resourceCost(sp)
    }

    val baseDamage = Pair(664, 786)
    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
