package sim.rotation

import character.Ability
import sim.Sim

class Rotation(val rules: List<Rule>) {
    fun next(sim: Sim): Ability? {
        return rules.firstOrNull {
            it.satisfied(sim)
        }?.ability
    }
}
