package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class ResourceLte(data: Map<String, String?>) : Criterion(Type.RESOURCE_LTE, data) {
    val amount: Int? = try {
        (data["amount"] as String).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'amount' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'amount' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        val resource = sim.subject.resource?.currentAmount
        return amount != null && resource != null && resource <= amount
    }
}
