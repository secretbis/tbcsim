package character.classes.shaman.buffs

import character.Ability
import sim.SimIteration

class StrengthOfEarthTotem(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25528
    override val name: String = "Strength of Earth Totem"

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun cast(free: Boolean) {
        TODO("Not yet implemented")
    }

    override fun castTimeMs(): Int = 0
    override fun gcdMs(): Int = sim.subject.totemGcd().toInt()
}
