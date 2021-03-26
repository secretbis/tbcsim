package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion
import kotlin.contracts.Returns

class IsExecutePhase(data: RotationRuleCriterion) : Criterion(Type.IS_EXECUTE_PHASE, data) {
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
        if (bool == false) {
            return !sp.sim.isExecutePhase()
        }
        return  bool != null && sp.sim.isExecutePhase()
    }
}
