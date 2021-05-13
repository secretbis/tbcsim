package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion
import character.Resource

class ResourceGte(data: RotationRuleCriterion) : Criterion(Type.RESOURCE_GTE, data) {
    val amount: Int? = try {
        (data.amount as Int).toInt().coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'amount' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'amount' must be an integer for criterion $type" }
        null
    }

    val resourceType: Resource.Type? = try {
        enumValueOf<Resource.Type>((data.resourceType as String))
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
        if(amount == null || resourceType == null) {return false}
        
        val res = sp.resources[resourceType]
        if(res == null) { 
            logger.warn { "The participant does not have the resource of type $resourceType (${this.type})" } 
            return false
        }

        return res.currentAmount >= amount
    }
}
