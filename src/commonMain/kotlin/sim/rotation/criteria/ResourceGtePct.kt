package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class ResourceGtePct(data: RotationRuleCriterion) : Criterion(Type.RESOURCE_GTE_PCT, data) {
    val pct: Int? = try {
        (data.pct as Int).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'pct' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'pct' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        return pct != null && sp.resource.currentAmount >= sp.resource.maxAmount * (pct / 100.0)
    }
}
