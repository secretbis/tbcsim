package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class IcyVeins : Ability() {
    companion object {
        const val name: String = "Icy Veins"
    }
    override val id: Int = 12472
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000
    override fun resourceCost(sp: SimParticipant): Double {
        return sp.character.klass.baseMana * 0.03
    }

    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
        // 20% haste for 20s
    }
}
