package sim.rotation

import character.Ability
import sim.SimIteration
import sim.SimParticipant
import sim.config.RotationRuleOptions

class Rule(
    val ability: Ability,
    val phase: Rotation.Phase,
    val criteria: List<Criterion>,
    val options: RotationRuleOptions?
) {
    fun satisfied(sp: SimParticipant): Boolean {
        return criteria.all { it.satisfied(sp) }
    }
}
