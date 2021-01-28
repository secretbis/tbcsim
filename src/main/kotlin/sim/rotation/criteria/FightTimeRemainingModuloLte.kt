package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class FightTimeRemainingModuloLte(data: Map<String, String?>) : Criterion(Type.FIGHT_TIME_REMAINING_MODULO_LTE, data) {
    val modulusSeconds: Int? = try {
        (data["modulusSeconds"] as String).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'modulusSeconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'modulusSeconds' must be an integer for criterion $type" }
        null
    }

    val seconds: Int? = try {
        (data["seconds"] as String).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(modulusSeconds == null || seconds == null) return false

        return sim.elapsedTimeMs % (modulusSeconds * 1000) <= seconds * 1000
    }
}
