package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class FightTimeElapsedGte(data: Map<String, String?>) : Criterion(Type.FIGHT_TIME_ELAPSED_GTE, data) {
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
        return if(seconds == null) { false} else { sim.elapsedTimeMs >= seconds * 1000 }
    }
}
