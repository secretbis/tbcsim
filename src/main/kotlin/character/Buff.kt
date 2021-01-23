package character

import sim.SimIteration

abstract class Buff {
    open class State {
        var currentStacks: Int = 0
        var currentCharges: Int = 0
        var appliedAtMs: Int = 0
    }

    abstract val name: String
    abstract val durationMs: Int

    open val hidden: Boolean = false
    open val maxStacks: Int = 0
    open val maxCharges: Int = 0

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal fun sharedState(name: String, sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.sharedBuffState.getOrDefault(name, stateFactory())
        sim.sharedBuffState[name] = state
        return state
    }

    internal fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.buffState.getOrDefault(this, stateFactory())
        sim.buffState[this] = state
        return state
    }

    open fun refresh(sim: SimIteration) {
        val state = state(sim)

        // Always refresh buff application time
        state.appliedAtMs = sim.elapsedTimeMs

        // Add stacks if it stacks
        if (maxStacks > 0 && state.currentStacks < maxStacks) {
            // Increase stacks
            state.currentStacks += state.currentStacks + 1
        }

        // Refresh charges if it has charges
        state.currentCharges = maxCharges
    }

    open fun reset(sim: SimIteration) {
        val state = state(sim)

        state.currentStacks = 0
        state.currentCharges = maxCharges
    }

    open fun isFinished(sim: SimIteration): Boolean {
        val state = state(sim)

        val noChargesLeft = maxCharges > 0 && state.currentCharges == 0

        // Duration of -1 means static
        val isExpired = durationMs != -1 && (sim.elapsedTimeMs > state.appliedAtMs + durationMs)

        return noChargesLeft || isExpired
    }

    abstract fun modifyStats(sim: SimIteration, stats: Stats): Stats
    abstract fun procs(sim: SimIteration): List<Proc>
}
