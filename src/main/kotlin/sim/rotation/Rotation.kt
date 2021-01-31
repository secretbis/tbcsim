package sim.rotation

import character.Ability
import sim.SimIteration

class Rotation(
    val rules: List<Rule>
) {
    enum class Phase {
        PRECOMBAT,
        COMBAT,
        RAID_OR_PARTY
    }

    fun precombat(sim: SimIteration) {
        return rules.filter {
            it.phase == Phase.PRECOMBAT ||
            it.phase == Phase.RAID_OR_PARTY
        }.forEach {
            it.ability.cast(sim, true)
        }
    }

    fun next(sim: SimIteration, onGcd: Boolean = false): Ability? {
        return rules.filter { it.phase == Phase.COMBAT }.firstOrNull {
            it.ability.available(sim) &&
            it.satisfied(sim) &&
            (!onGcd || (onGcd && it.ability.castableOnGcd)) &&
            it.ability.resourceCost(sim) <= sim.resource.currentAmount
        }?.ability
    }
}
