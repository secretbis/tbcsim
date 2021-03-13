package character

import mu.KotlinLogging
import sim.SimParticipant

abstract class Buff {
    open class State {
        val logger = KotlinLogging.logger {}
        var currentStacks: Int = 0
            set(value) {
                field = if(value < 0) {
                    logger.warn { "Buff currentStacks cannot be set below 0 - this is probably a bug" }
                    0
                } else value
            }
        var currentCharges: Int = 0
            set(value) {
                field = if(value < 0) {
                    logger.warn { "currentCharges cannot be set below 0 - this is probably a bug" }
                    0
                } else value
            }
        var appliedAtMs: Int = 0
        var lastTickMs: Int = -1
        var tickCount: Int = 0
    }

    open val id: Int = -1
    abstract val name: String
    abstract val durationMs: Int
    open val mutex: List<Mutex> = listOf(Mutex.NONE)

    open val hidden: Boolean = false
    open val maxStacks: Int = 0
    open val maxCharges: Int = 0

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal open fun state(sp: SimParticipant, stateKey: String = name): State {
        // Create state object if it does not exist, and return it
        val state = sp.buffState[stateKey] ?: stateFactory()
        sp.buffState[stateKey] = state
        return state
    }

    open fun refresh(sp: SimParticipant) {
        val state = state(sp)

        // Always refresh buff application time
        state.appliedAtMs = sp.sim.elapsedTimeMs
        state.lastTickMs = sp.sim.elapsedTimeMs

        // Add stacks if it stacks
        if (maxStacks > 0 && state.currentStacks < maxStacks) {
            // Increase stacks
            state.currentStacks += 1
        }

        // Refresh charges if it has charges
        state.currentCharges = maxCharges
        state.tickCount = 0
    }

    open fun reset(sp: SimParticipant) {
        val state = state(sp)

        state.currentStacks = 0
        state.currentCharges = maxCharges
        state.tickCount = 0
    }

    open fun remainingDurationMs(sp: SimParticipant): Int {
        return if(durationMs == -1) {
            // 24 hours in ms
            1000 * 60 * 60 * 24
        } else {
            val state = state(sp)
            ((state.appliedAtMs + durationMs) - sp.sim.elapsedTimeMs).coerceAtLeast(0)
        }
    }

    open fun modifyStats(sp: SimParticipant): Stats? {
        return null
    }

    open fun activeTrinketAbility(sp: SimParticipant): Ability? {
        return null
    }

    open fun procs(sp: SimParticipant): List<Proc> = listOf()
}
