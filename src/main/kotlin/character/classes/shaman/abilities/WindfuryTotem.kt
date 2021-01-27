package character.classes.shaman.abilities

import character.Ability
import sim.SimIteration

class WindfuryTotem: Ability() {
    companion object {
        const val name = "Windfury Totem"
    }

    override val id: Int = 25587
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
