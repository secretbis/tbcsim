package sim.rotation

import mu.KotlinLogging
import sim.SimIteration
import sim.rotation.criteria.*

abstract class Criterion(val type: Type, val data: Map<String, String?>) {
    enum class Type {
        RESOURCE_GTE,
        RESOURCE_LTE,
        ABILITY_COOLDOWN_GTE,
        ABILITY_COOLDOWN_LTE,
        BUFF_DURATION_GTE,
        BUFF_DURATION_LTE,
        DEBUFF_DURATION_GTE,
        DEBUFF_DURATION_LTE,
        FIGHT_TIME_ELAPSED_GTE,
        FIGHT_TIME_REMAINING_MODULO_LTE
    }

    companion object {
        val logger = KotlinLogging.logger {}
        fun fromString(typeName: String?, data: Map<String, String?>): Criterion? {
            if(typeName == null) return null

            val type = Type.values().asList().find { it.name == typeName }
            return when(type) {
                Type.RESOURCE_GTE -> ResourceGte(data)
                Type.RESOURCE_LTE -> ResourceLte(data)
                Type.ABILITY_COOLDOWN_GTE -> AbilityCooldownGte(data)
                Type.ABILITY_COOLDOWN_LTE -> AbilityCooldownLte(data)
                Type.BUFF_DURATION_GTE -> BuffDurationGte(data)
                Type.BUFF_DURATION_LTE -> BuffDurationLte(data)
                Type.DEBUFF_DURATION_GTE -> DebuffDurationGte(data)
                Type.DEBUFF_DURATION_LTE -> DebuffDurationLte(data)
                Type.FIGHT_TIME_ELAPSED_GTE -> FightTimeElapsedGte(data)
                Type.FIGHT_TIME_REMAINING_MODULO_LTE -> FightTimeRemainingModuloLte(data)
                else -> {
                    logger.warn { "Unknown rotation criterion: $typeName" }
                    null
                }
            }
        }
    }

    abstract fun satisfied(sim: SimIteration): Boolean
}
