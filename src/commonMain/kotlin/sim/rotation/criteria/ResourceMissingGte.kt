package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class ResourceMissingGte(data: RotationRuleCriterion) : Criterion(Type.RESOURCE_MISSING_GTE, data) {
    val amount: Int? = try {
        (data.amount as Int).coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'amount' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'amount' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        return amount != null && sp.resource.currentAmount <= sp.resource.maxAmount - amount
    }
}
