package character.classes.shaman.buffs

import character.Ability
import sim.Sim

class FlametongueWeapon(sim: Sim) : Ability(sim) {
    override val id: Int = 25489
    override val name: String = "Flametongue Weapon (Rank 7)"

    override fun available(): Boolean {
        return true
    }

    override fun cast(free: Boolean) {
        TODO("Not yet implemented")
    }

    override fun castTimeMs(): Double = 0.0
    override fun gcdMs(): Double = 0.0
}
