package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class BuffDurationLte(data: RotationRuleCriterion) : Criterion(Type.BUFF_DURATION_LTE, data) {
    val buff: String? = try {
        data.buff as String
    } catch (e: Exception) {
        logger.warn { "Field 'buff' is required for criterion $type" }
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
        if(buff == null || seconds == null) return false

        val buff = sp.buffs[buff]
        return buff == null || buff.remainingDurationMs(sp) <= (seconds * 1000)
    }
}
