package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion
import character.Resource

class ResourcePctLte(data: RotationRuleCriterion) : Criterion(Type.RESOURCE_PCT_LTE, data) {
    val pct: Int? = try {
        (data.pct as Int).coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'pct' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'pct' must be an integer for criterion $type" }
        null
    }

    val resourceType: Resource.Type? = try {
        Resource.Type.valueOf((data.resourceType as String))
    } catch (e: NullPointerException) {
        logger.warn { "Field 'resourceType' is required for criterion $type" }
        null
    } catch (e: IllegalArgumentException) {
        logger.warn { "Field 'resourceType' is not a valid Resource Type for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'resourceType' must be a string for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        val res = sp.resources[resourceType]
        if(pct == null || resourceType == null || res == null) {return false}
        return res.currentAmount <= res.maxAmount * (pct / 100.0)
    }
}
