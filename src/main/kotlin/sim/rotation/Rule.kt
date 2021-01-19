package sim.rotation

import character.Ability
import sim.Sim

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

    fun satisfied(sim: Sim): Boolean {
        return conditions.all { it.satisfied(sim) }
    }
}
