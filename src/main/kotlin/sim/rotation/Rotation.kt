package sim.rotation

import character.Ability
import sim.SimIteration

class Rotation(val rules: List<Rule>) {
    fun next(sim: SimIteration): Ability? {
        return rules.firstOrNull {
            it.satisfied(sim)
        }?.ability
    }
}
