package sim.rotation

import sim.rotation.criteria.Criterion

class Condition(val type: Type, val criterion: Criterion) {
    enum class Type {
        PRECOMBAT,
        RESOURCE_GTE,
        RESOURCE_LTE,
        SPELL_COOLDOWN_GTE,
        SPELL_COOLDOWN_LTE,
        BUFF_DURATION_GTE,
        BUFF_DURATION_LTE,
        TIME_TO_EXECUTE_LTE
    }
}