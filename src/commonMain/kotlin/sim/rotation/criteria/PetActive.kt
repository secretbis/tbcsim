package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class PetActive(data: RotationRuleCriterion) : Criterion(Type.PET_ACTIVE, data) {
    val bool: Boolean? = try {
        (data.bool as Boolean)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'Bool' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'Bool' must be a Boolean for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        if(sp.pet == null) return false

        if (bool == false) {
            return !sp.pet.isActive()
        }
        return bool != null && sp.pet.isActive()
    }
}
