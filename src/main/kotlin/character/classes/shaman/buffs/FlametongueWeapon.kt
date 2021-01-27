package character.classes.shaman.buffs

import character.Ability
import sim.SimIteration

class FlametongueWeapon : Ability() {
    companion object {
        const val name = "Flametongue Weapon"
    }
    override val id: Int = 25489
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        TODO("Not yet implemented")
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = 0
}
