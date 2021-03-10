package sim.rotation

import character.Ability
import sim.SimIteration
import sim.config.RotationRuleOptions

class Rule(
    val ability: Ability,
    val phase: Rotation.Phase,
    val criteria: List<Criterion>,
    val options: RotationRuleOptions?
) {
    fun satisfied(sim: SimIteration): Boolean {
        return criteria.all { it.satisfied(sim) }
    }
}
