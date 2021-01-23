package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class GraceOfAirTotem(sim: SimIteration) : Ability(sim) {
    override val id: Int = 25359
    override val name: String = "Grace of Air Totem"

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun cast(free: Boolean) {
        TODO("Not yet implemented")
    }

    override val baseCastTimeMs: Int = 0
    override val gcdMs: Int = sim.subject.totemGcd().toInt()
}
