package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class ResourceLtePct(data: Map<String, String?>) : Criterion(Type.RESOURCE_LTE_PCT, data) {
    val pct: Int? = try {
        (data["pct"] as String).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'pct' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'pct' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        return pct != null && sim.resource.currentAmount <= sim.resource.maxAmount * (pct / 100.0)
    }
}
