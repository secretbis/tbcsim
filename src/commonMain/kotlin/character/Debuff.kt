package character

import sim.SimIteration

abstract class Debuff : Buff() {
    open val tickDeltaMs: Int = -1

    // TODO: This is probably pretty slow
    open fun shouldTick(sim: SimIteration): Boolean {
        if(tickDeltaMs == -1) return false

        val state = state(sim)
        return (sim.elapsedTimeMs - state.appliedAtMs) % tickDeltaMs == 0
    }

    open fun tick(sim: SimIteration) {
        // Do nothing by default
    }

    override fun sharedState(name: String, sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.sharedDebuffState[name] ?: stateFactory()
        sim.sharedDebuffState[name] = state
        return state
    }

    override fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.debuffState[this] ?: stateFactory()
        sim.debuffState[this] = state
        return state
    }
}
