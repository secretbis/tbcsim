package sim.rotation.criteria

import sim.SimIteration
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class FightTimeRemainingGte(data: RotationRuleCriterion) : Criterion(Type.FIGHT_TIME_REMAINING_GTE, data) {
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
        return if(seconds == null) { false } else { sim.opts.durationMs - sim.elapsedTimeMs >= seconds * 1000 }
    }
}
