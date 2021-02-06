package sim.rotation

import character.Ability
import sim.SimIteration

class Rotation(
    val rules: List<Rule>,
    val autoAttack: Boolean
) {
    enum class Phase {
        PRECOMBAT,
        COMBAT,
        RAID_OR_PARTY
    }

    fun castAllPrecombat(sim: SimIteration) {
        return rules.filter {
            it.phase == Phase.PRECOMBAT ||
            it.phase == Phase.RAID_OR_PARTY
        }.forEach {
            it.ability.cast(sim)
        }
    }

    fun raidOrParty(sim: SimIteration): List<Ability> {
        return rules.filter { it.phase == Phase.RAID_OR_PARTY }.map { it.ability }
    }

    fun precombat(sim: SimIteration): List<Ability> {
        return rules.filter { it.phase == Phase.PRECOMBAT }.map { it.ability }
    }

    fun combat(sim: SimIteration): List<Ability> {
        return rules.filter { it.phase == Phase.COMBAT }.map { it.ability }
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
