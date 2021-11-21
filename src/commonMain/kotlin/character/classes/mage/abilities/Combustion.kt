package character.classes.mage.abilities

import character.classes.mage.buffs.Combustion as CombustionBuff
import character.Ability
import sim.SimParticipant

class Combustion : Ability() {
    companion object {
        const val name: String = "Combustion"
    }
    override val id: Int = 11129
    override val name: String = Companion.name
    override val icon: String = "spell_fire_sealoffire.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0
    override fun castTimeMs(sp: SimParticipant): Int = 0
    override fun cooldownMs(sp: SimParticipant): Int = 180000

    override fun cast(sp: SimParticipant) {
        sp.addBuff(CombustionBuff())
    }
}
