package sim.rotation

import character.Ability
import mu.KotlinLogging
import sim.SimParticipant

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

    fun castAllPrecombat(sp: SimParticipant) {
        return rules.filter {
            it.phase == Phase.PRECOMBAT ||
            it.phase == Phase.RAID_OR_PARTY
        }.forEach {
            if(it.ability.available(sp)) {
                it.ability.cast(sp)
            } else {
                logger.warn { "Could not cast precombat ability (not available): ${it.ability.name}" }
            }
        }
    }

    fun raidOrParty(sp: SimParticipant): List<Ability> {
        return rules.filter { it.phase == Phase.RAID_OR_PARTY }.map { it.ability }
    }

    fun precombat(sp: SimParticipant): List<Ability> {
        return rules.filter { it.phase == Phase.PRECOMBAT }.map { it.ability }
    }

    fun combat(sp: SimParticipant): List<Ability> {
        return rules.filter { it.phase == Phase.COMBAT }.map { it.ability }
    }

    fun next(sp: SimParticipant, onGcd: Boolean = false): Rule? {
        return rules.filter { it.phase == Phase.COMBAT }.firstOrNull {
            it.ability.available(sp) &&
            it.satisfied(sp) &&
            (!onGcd || (onGcd && it.ability.castableOnGcd)) &&
            it.ability.resourceCost(sp) <= sp.resource.currentAmount
        }
    }
}
