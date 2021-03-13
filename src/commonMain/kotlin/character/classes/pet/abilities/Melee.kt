package character.classes.pet.abilities

import character.Ability
import sim.SimParticipant

class Melee : Ability() {
    override val id: Int = 1
    override val name: String = "Melee"
    override fun gcdMs(sp: SimParticipant): Int = 0

//    val basePetAttackSpeed: Double = 2000.0
//    override fun cooldownMs(sp: SimParticipant): Int {
//        val isFrenzied = sp.buffs[Frenzy.name]
//    }

    override fun cast(sp: SimParticipant) {

    }
}
