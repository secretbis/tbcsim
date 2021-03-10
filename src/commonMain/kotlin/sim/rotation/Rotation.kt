package sim.rotation

import character.Ability
import mu.KotlinLogging
import sim.SimIteration

class Rotation(
    val rules: List<Rule>,
    val autoAttack: Boolean
) {
    private val logger = KotlinLogging.logger {}
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
            if(it.ability.available(sim)) {
                it.ability.cast(sim)
            } else {
                logger.warn { "Could not cast precombat ability (not available): ${it.ability.name}" }
            }
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

    fun next(sim: SimIteration, onGcd: Boolean = false): Rule? {
        return rules.filter { it.phase == Phase.COMBAT }.firstOrNull {
            it.ability.available(sim) &&
            it.satisfied(sim) &&
            (!onGcd || (onGcd && it.ability.castableOnGcd)) &&
            it.ability.resourceCost(sim) <= sim.resource.currentAmount
        }
    }
}
