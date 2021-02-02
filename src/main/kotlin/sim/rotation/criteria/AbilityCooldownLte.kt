package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class AbilityCooldownLte(data: Map<String, String?>) : Criterion(Type.ABILITY_COOLDOWN_LTE, data) {
    val ability: String? = try {
        data["ability"] as String
    } catch (e: Exception) {
        logger.warn { "Field 'buff' is required for criterion $type" }
        null
    }

    val seconds: Double? = try {
        (data["seconds"] as String).toDouble().coerceAtLeast(0.0)
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
