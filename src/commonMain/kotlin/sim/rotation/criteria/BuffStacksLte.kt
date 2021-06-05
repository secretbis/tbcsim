package sim.rotation.criteria

import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.Criterion

class BuffStacksLte(data: RotationRuleCriterion) : Criterion(Type.BUFF_STACKS_LTE, data) {
    val buff: String? = try {
        data.buff as String
    } catch (e: Exception) {
        logger.warn { "Field 'buff' is required for criterion $type" }
        null
    }

    val stacks: Int? = try {
        (data.stacks as Int).coerceAtLeast(0)
    } catch (e: NullPointerException) {
        logger.warn { "Field 'stacks' is required for criterion $type" }
        null
    } catch(e: Exception) {
        logger.warn { "Field 'stacks' must be an integer for criterion $type" }
        null
    }

    override fun satisfied(sp: SimParticipant): Boolean {
        if(buff == null || stacks == null) return false

        val state = sp.buffs[buff]?.state(sp)
        return state == null || state.currentStacks <= stacks
    }
}
