package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class FightTimeRemainingLte(data: RotationRuleCriterion) : Criterion(Type.FIGHT_TIME_REMAINING_LTE, data) {
    val seconds: Double? = try {
        (data.seconds as Double).coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        return if(seconds == null) { false } else { sp.sim.opts.durationMs - sp.sim.elapsedTimeMs <= seconds * 1000 }
    }
}
