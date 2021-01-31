package character

import sim.SimIteration

abstract class Ability {
    open class State {
        var cooldownStartMs: Int = -1
    }

    enum class SharedCooldown {
        NONE,
        SHAMAN_SHOCK,
        POTION,
        DRUMS
    }

    abstract val id: Int
    abstract val name: String

    abstract fun gcdMs(sim: SimIteration): Int
    open val castableOnGcd = false

    open fun cooldownMs(sim: SimIteration): Int = 0
    open val sharedCooldown: SharedCooldown = SharedCooldown.NONE

    open fun resourceCost(sim: SimIteration): Double = 0.0
    open val resourceType: Resource.Type = Resource.Type.MANA

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal open fun sharedState(type: SharedCooldown, sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.sharedAbilityState.getOrDefault(type.toString(), stateFactory())
        sim.sharedAbilityState[type.toString()] = state
        return state
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
        val state = if(sharedCooldown == SharedCooldown.NONE) {
            state(sim)
        } else {
            sharedState(sharedCooldown, sim)
        }

        return state.cooldownStartMs == -1 || (state.cooldownStartMs + cooldownMs(sim) <= sim.elapsedTimeMs)
    }

    abstract fun cast(sim: SimIteration, free: Boolean = false)
    open fun afterCast(sim: SimIteration) {
        // Store individual cooldown state
        val state = state(sim)
        state.cooldownStartMs = sim.elapsedTimeMs

        // Store shared cooldown state
        if(sharedCooldown != SharedCooldown.NONE) {
            val sharedState = sharedState(sharedCooldown, sim)
            sharedState.cooldownStartMs = sim.elapsedTimeMs
        }
    }

    // Base cast time should include talent reductions, and other static modifiers
    abstract val baseCastTimeMs: Int
    // This defines whether an ability is a DoT or not
    open val baseDurationMs: Int = 0

    // Final cast time accounting for haste
    fun castTimeMs(sim: SimIteration): Int {
        return (baseCastTimeMs / sim.spellHasteMultiplier()).toInt()
    }
}
