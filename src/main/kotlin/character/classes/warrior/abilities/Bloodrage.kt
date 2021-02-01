package character.classes.warrior.abilities

import character.Ability
import character.Resource
import sim.SimIteration

class Bloodrage : Ability() {
    companion object {
        const val name: String = "Bloodrage"
    }

    override val id: Int = 2687
    override val name: String = Companion.name

    override fun cooldownMs(sim: SimIteration): Int = 60000
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()

    override fun cast(sim: SimIteration, free: Boolean) {
        // Close enough
        sim.addResource(20, Resource.Type.RAGE)
    }
}
