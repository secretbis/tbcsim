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

    val raidOrParty: List<Rule> = rules.filter { it.phase == Phase.RAID_OR_PARTY }
    val precombat: List<Rule> = rules.filter { it.phase == Phase.PRECOMBAT }
    val combat: List<Rule> = rules.filter { it.phase == Phase.COMBAT }

    val raidOrPartyAbilities: List<Ability> = raidOrParty.map { it.ability }
    val precombatAbilities: List<Ability> = precombat.map { it.ability }
    val combatAbilities: List<Ability> = combat.map { it.ability }

    fun castAllPrecombat(sp: SimParticipant) {
        return precombat.forEach {
            if(it.ability.available(sp)) {
                it.ability.cast(sp)
            } else {
                logger.warn { "Could not cast precombat ability (not available): ${it.ability.name}" }
            }
        }
    }

    fun castAllRaidBuffs(sp: SimParticipant) {
        return raidOrParty.forEach {
            it.ability.cast(sp)
        }
    }

    fun next(sp: SimParticipant, onGcd: Boolean = false): Rule? {
        return combat.firstOrNull {
            (!onGcd || (onGcd && it.ability.castableOnGcd)) &&
            it.ability.available(sp) &&
            it.satisfied(sp) &&
            it.ability.resourceCost(sp) <= sp.resource.currentAmount
        }
    }
}
