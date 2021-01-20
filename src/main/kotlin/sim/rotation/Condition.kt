package sim.rotation

import sim.SimIteration
import sim.rotation.criteria.Criterion

class Condition(val type: Type, val criteria: List<Criterion>) {
    enum class Type {
        RESOURCE_GTE,
        RESOURCE_LTE,
        SPELL_COOLDOWN_GTE,
        SPELL_COOLDOWN_LTE,
        BUFF_DURATION_GTE,
        BUFF_DURATION_LTE,
        TIME_TO_EXECUTE_LTE
    }

    fun satisfied(sim: SimIteration): Boolean {
        return criteria.all { it.satisfied(sim) }
    }
}
