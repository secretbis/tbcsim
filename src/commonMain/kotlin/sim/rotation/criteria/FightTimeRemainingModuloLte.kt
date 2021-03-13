package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class FightTimeRemainingModuloLte(data: RotationRuleCriterion) : Criterion(Type.FIGHT_TIME_REMAINING_MODULO_LTE, data) {
    val modulusSeconds: Double? = try {
        (data.modulusSeconds as Int).toDouble().coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'modulusSeconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'modulusSeconds' must be an integer for criterion $type" }
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

    override fun satisfied(sp: SimParticipant): Boolean {
        if(modulusSeconds == null || seconds == null) return false
        return sp.sim.elapsedTimeMs % (modulusSeconds * 1000) <= seconds * 1000
    }
}
