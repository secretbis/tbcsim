package character

import sim.SimIteration

abstract class Debuff : Buff() {
    open val tickDeltaMs: Int = -1

    // TODO: This is probably pretty slow
    open fun shouldTick(sim: SimIteration): Boolean {
        if(tickDeltaMs == -1) return false

        val state = state(sim)
        if(sim.elapsedTimeMs == state.appliedAtMs) return false
        if(sim.elapsedTimeMs == state.appliedAtMs + 1) return false

        return (sim.elapsedTimeMs - state.appliedAtMs + 1) % tickDeltaMs == 0
    }

    open fun tick(sim: SimIteration) {
        // Do nothing by default
    }

    override fun state(sim: SimIteration, stateKey: String): State {
        // Create state object if it does not exist, and return it
        val state = sim.debuffState[stateKey] ?: stateFactory()
        sim.debuffState[stateKey] = state
        return state
    }
}
