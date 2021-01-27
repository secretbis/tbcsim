package character

import data.model.Item
import sim.SimIteration

abstract class Ability {
    open class State {
        var cooldownStartMs: Int = -1
    }

    abstract val id: Int
    abstract val name: String
    abstract fun gcdMs(sim: SimIteration): Int
    open fun cooldownMs(sim: SimIteration): Double = 0.0

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal fun state(sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        var state = sim.abilityState[name]
        if(state == null) {
            state = stateFactory()
            sim.abilityState[name] = state
        }
        return state
    }

    open fun available(sim: SimIteration): Boolean {
        val state = state(sim)
        return state.cooldownStartMs == -1 || (state.cooldownStartMs + cooldownMs(sim) <= sim.elapsedTimeMs)
    }

    // TODO: Resource costs
    abstract fun cast(sim: SimIteration, free: Boolean = false)
    open fun afterCast(sim: SimIteration) {
        val state = state(sim)
        state.cooldownStartMs = sim.elapsedTimeMs
    }

    // Base cast time should include talent reductions, and other static modifiers
    abstract val baseCastTimeMs: Int
    // This defines whether an ability is a DoT or not
    open val baseDurationMs: Int = 0

    // Final cast time accounting for haste
    fun castTimeMs(sim: SimIteration): Int {
        return (baseCastTimeMs / sim.subject.spellHasteMultiplier()).toInt()
    }

    // AP and spell damage coefficients
    // TODO: Verify that these formulas reflect TBC mechanics
    // https://wowwiki.fandom.com/wiki/Spell_power
    open val spellPowerCoeff: Double
        get() {
            // DoT
            return if(baseDurationMs == 0) {
                // Most instant spells are treated as 1.5s cast time for coeff purposes
                baseCastTimeMs.coerceAtLeast(1500) / 3500.0
            } else {
                baseDurationMs / 15000.0
            }
        }
}
