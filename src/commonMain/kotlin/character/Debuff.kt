package character

import sim.SimParticipant

abstract class Debuff(val owner: SimParticipant) : Buff() {
    open val tickDeltaMs: Int = -1

    open fun shouldTick(sp: SimParticipant): Boolean {
        if(tickDeltaMs == -1) return false

        val state = state(sp)

        // Never tick the debuff on the same server tick it was applied
        if(sp.sim.elapsedTimeMs == state.appliedAtMs) return false

        val shouldTick = sp.sim.elapsedTimeMs >= state.lastTickMs + tickDeltaMs - sp.sim.opts.stepMs
        if(shouldTick) {
            state.tickCount++
            state.lastTickMs = sp.sim.elapsedTimeMs
        }

        return shouldTick
    }

    open fun tick(sp: SimParticipant) {
        // Do nothing by default
    }

    override fun state(sp: SimParticipant, stateKey: String): State {
        // Create state object if it does not exist, and return it
        val state = sp.debuffState[stateKey] ?: stateFactory()
        sp.debuffState[stateKey] = state
        return state
    }
}
