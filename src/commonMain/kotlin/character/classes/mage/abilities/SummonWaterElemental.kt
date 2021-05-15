package character.classes.mage.abilities

import character.Ability
import sim.SimParticipant

class SummonWaterElemental : Ability() {
    companion object {
        const val name: String = "Summon Water Elemental"
    }
    override val id: Int = 30451
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = sp.spellGcd().toInt()
    override fun castTimeMs(sp: SimParticipant): Int  = 0

    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun resourceCost(sp: SimParticipant): Double {
        return sp.character.klass.baseMana * 0.16
    }

    val baseDamage = Pair(668, 772)
    override fun cast(sp: SimParticipant) {
        TODO("Not yet implemented")
    }
}
