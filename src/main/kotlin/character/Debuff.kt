package character

import sim.SimIteration

abstract class Debuff : Buff() {
    override fun sharedState(name: String, sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.sharedDebuffState.getOrDefault(name, stateFactory())
        sim.sharedDebuffState[name] = state
        return state
    }

    override fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.debuffState.getOrDefault(this, stateFactory())
        sim.debuffState[this] = state
        return state
    }
}
