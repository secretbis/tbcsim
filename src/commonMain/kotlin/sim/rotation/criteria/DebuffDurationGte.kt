package sim.rotation.criteria

import sim.SimIteration
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class DebuffDurationGte(data: RotationRuleCriterion) : Criterion(Type.DEBUFF_DURATION_GTE, data) {
    val debuff: String? = try {
        data.debuff as String
    } catch (e: Exception) {
        logger.warn { "Field 'debuff' is required for criterion $type" }
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
        if(debuff == null || seconds == null) return false

        val debuff = sim.debuffs[debuff]
        return debuff != null && debuff.remainingDurationMs(sim) >= (seconds * 1000)
    }
}
