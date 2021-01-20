package character.classes.shaman.buffs

import character.Ability
import sim.SimIteration

class FlametongueWeapon(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25489
    override val name: String = "Flametongue Weapon (Rank 7)"

    override fun available(): Boolean {
        return true
    }

    override fun cast(free: Boolean) {
        TODO("Not yet implemented")
    }

    override fun castTimeMs(): Int = 0
    override fun gcdMs(): Int = 0
}
