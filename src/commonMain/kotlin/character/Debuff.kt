package character

import sim.SimIteration

abstract class Debuff : Buff() {
    open val tickDeltaMs: Int = -1

    open fun shouldTick(sim: SimIteration): Boolean {
        if(tickDeltaMs == -1) return false

        val state = state(sim)

        // Never tick the debuff on the same server tick it was applied
        if(sim.elapsedTimeMs == state.appliedAtMs) return false

        val shouldTick = sim.elapsedTimeMs >= state.lastTickMs + tickDeltaMs - sim.opts.stepMs
        if(shouldTick) {
            state.tickCount++
            state.lastTickMs = sim.elapsedTimeMs
        }

        return shouldTick
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
