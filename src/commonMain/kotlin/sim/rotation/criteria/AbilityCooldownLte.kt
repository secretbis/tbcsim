package sim.rotation.criteria

import sim.SimIteration
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class AbilityCooldownLte(data: RotationRuleCriterion) : Criterion(Type.ABILITY_COOLDOWN_LTE, data) {
    val ability: String? = try {
        data.ability as String
    } catch (e: Exception) {
        logger.warn { "Field 'ability' is required for criterion $type" }
        null
    }

    val seconds: Double? = try {
        (data.seconds as Double).coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(ability == null || seconds == null) return false

        val abilityState = sim.abilityState[ability]
        return abilityState != null && (sim.elapsedTimeMs - abilityState.cooldownStartMs).coerceAtLeast(0) <= seconds * 1000.0
    }
}
