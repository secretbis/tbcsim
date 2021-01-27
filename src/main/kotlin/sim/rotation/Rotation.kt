package sim.rotation

import character.Ability
import sim.SimIteration

class Rotation(
    val rules: List<Rule>
) {
    enum class Phase {
        PRECOMBAT,
        COMBAT
    }

    fun precombat(sim: SimIteration) {
        return rules.filter {
            it.phase == Phase.PRECOMBAT
        }.forEach {
            it.ability.cast(sim, true)
        }
    }

    fun next(sim: SimIteration): Ability? {
        return rules.filter { it.phase == Phase.COMBAT }.firstOrNull {
            it.ability.available(sim) && it.satisfied(sim)
        }?.ability
    }
}
