package sim.rotation

import character.Ability
import sim.SimIteration

class Rule(
    val conditions: List<Condition>,
    val ability: Ability,
    // Almost all of these will be combat rules - precombat is processed separately
    val type: Type = Type.COMBAT
) {
    enum class Type {
        PRECOMBAT,
        COMBAT
    }

    fun satisfied(sim: SimIteration): Boolean {
        return conditions.all { it.satisfied(sim) }
    }
}
