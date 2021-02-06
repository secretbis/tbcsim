package sim.rotation

import character.Ability
import sim.SimIteration

class Rule(
    val ability: Ability,
    val phase: Rotation.Phase,
    val criteria: List<Criterion>,
) {
    fun satisfied(sim: SimIteration): Boolean {
        return criteria.all { it.satisfied(sim) }
    }
}
