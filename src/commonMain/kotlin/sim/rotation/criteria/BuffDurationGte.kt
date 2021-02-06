package sim.rotation.criteria

import sim.SimIteration
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class BuffDurationGte(data: RotationRuleCriterion) : Criterion(Type.BUFF_DURATION_GTE, data) {
    val buff: String? = try {
        data.buff as String
    } catch (e: Exception) {
        logger.warn { "Field 'buff' is required for criterion $type" }
        null
    }

    val seconds: Double? = try {
        (data.seconds as Int).toDouble().coerceAtLeast(0.0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(buff == null || seconds == null) return false

        val buff = sim.buffs.find { it.name == buff }
        return buff != null && buff.remainingDurationMs(sim) >= (seconds * 1000)
    }
}
