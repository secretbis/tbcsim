package sim.rotation.criteria

import sim.SimIteration
import sim.rotation.Criterion

class DebuffDurationGte(data: Map<String, String?>) : Criterion(Type.DEBUFF_DURATION_GTE, data) {
    val debuff: String? = try {
        data["debuff"] as String
    } catch (e: Exception) {
        logger.warn { "Field 'debuff' is required for criterion $type" }
        null
    }

    val seconds: Int? = try {
        (data["seconds"] as String).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'seconds' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'seconds' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sim: SimIteration): Boolean {
        if(debuff == null || seconds == null) return false

        val debuff = sim.debuffs.find { it.name == debuff }
        return debuff != null && debuff.remainingDurationMs(sim) >= (seconds * 1000)
    }
}
