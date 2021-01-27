package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class GraceOfAirTotem : Ability() {
    companion object {
        const val name = "Grace of Air Totem"
    }

    override val id: Int = 25359
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        TODO("Not yet implemented")
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.subject.totemGcd().toInt()
}
