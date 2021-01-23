package character

import sim.SimIteration

abstract class Ability(val sim: SimIteration) {
    class State {
        var cooldownStartMs: Int = -1
    }

    abstract val id: Int
    abstract val name: String

    open val cooldownMs: Double = 0.0

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.abilityState.getOrDefault(this, stateFactory())
        sim.abilityState[name] = state
        return state
    }

    open fun available(sim: SimIteration): Boolean {
        val state = state(sim)
        return state.cooldownStartMs == -1 || (state.cooldownStartMs + cooldownMs <= sim.elapsedTimeMs)
    }

    // TODO: Resource costs
    open fun cast(free: Boolean = false) {
        val state = state(sim)
        state.cooldownStartMs = sim.elapsedTimeMs
    }

    abstract fun castTimeMs(): Int
    abstract fun gcdMs(): Int
}
