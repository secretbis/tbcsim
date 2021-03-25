package sim.rotation

import mu.KotlinLogging
import sim.SimIteration
import sim.SimParticipant
import sim.config.RotationRuleCriterion
import sim.rotation.criteria.*

abstract class Criterion(val type: Type, val data: RotationRuleCriterion) {
    enum class Type {
        RESOURCE_GTE_PCT,
        RESOURCE_LTE_PCT,
        RESOURCE_MISSING_GTE,
        ABILITY_COOLDOWN_GTE,
        ABILITY_COOLDOWN_LTE,
        BUFF_DURATION_GTE,
        BUFF_DURATION_LTE,
        DEBUFF_DURATION_GTE,
        DEBUFF_DURATION_LTE,
        FIGHT_TIME_ELAPSED_GTE,
        FIGHT_TIME_REMAINING_MODULO_LTE,
        MAIN_HAND_SWING_TIMER_GTE,
        FIGHT_TIME_REMAINING_GTE,
        FIGHT_TIME_REMAINING_LTE,
        IS_EXECUTE_PHASE
    }

    companion object {
        val logger = KotlinLogging.logger {}
        fun fromString(typeName: String?, data: RotationRuleCriterion): Criterion? {
            if(typeName == null) return null

            val type = Type.values().asList().find { it.name == typeName }
            return when(type) {
                Type.RESOURCE_GTE_PCT -> ResourceGtePct(data)
                Type.RESOURCE_LTE_PCT -> ResourceLtePct(data)
                Type.RESOURCE_MISSING_GTE -> ResourceMissingGte(data)
                Type.ABILITY_COOLDOWN_GTE -> AbilityCooldownGte(data)
                Type.ABILITY_COOLDOWN_LTE -> AbilityCooldownLte(data)
                Type.BUFF_DURATION_GTE -> BuffDurationGte(data)
                Type.BUFF_DURATION_LTE -> BuffDurationLte(data)
                Type.DEBUFF_DURATION_GTE -> DebuffDurationGte(data)
                Type.DEBUFF_DURATION_LTE -> DebuffDurationLte(data)
                Type.FIGHT_TIME_ELAPSED_GTE -> FightTimeElapsedGte(data)
                Type.FIGHT_TIME_REMAINING_MODULO_LTE -> FightTimeRemainingModuloLte(data)
                Type.MAIN_HAND_SWING_TIMER_GTE -> MainHandSwingTimerGte(data)
                Type.FIGHT_TIME_REMAINING_GTE -> FightTimeRemainingGte(data)
                Type.FIGHT_TIME_REMAINING_LTE -> FightTimeRemainingLte(data)
                Type.IS_EXECUTE_PHASE -> IsExecutePhase(data)
                else -> {
                    logger.warn { "Unknown rotation criterion: $typeName" }
                    null
                }
            }
        }
    }

    // Base type for rotation state containers
    open class State

    internal open fun stateFactory(): State {
        return State()
    }

    internal open fun state(sp: SimParticipant): State {
        // Create state object if it does not exist, and return it
        val state = sp.rotationState[type] ?: stateFactory()
        sp.rotationState[type] = state
        return state
    }

    abstract fun satisfied(sp: SimParticipant): Boolean
}
