package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class DebuffDurationLte(data: RotationRuleCriterion) : Criterion(Type.DEBUFF_DURATION_LTE, data) {
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

    override fun satisfied(sp: SimParticipant): Boolean {
        if(debuff == null || seconds == null) return false

        val debuff = sp.target().debuffs[debuff]
        return debuff == null || debuff.remainingDurationMs(sp.target()) <= (seconds * 1000)
    }
}
