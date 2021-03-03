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
        RUNE_OR_MANA_GEM,
        DRUMS
    }

    abstract val id: Int
    abstract val name: String

    abstract fun gcdMs(sim: SimIteration): Int
    open val castableOnGcd = false

    open fun cooldownMs(sim: SimIteration): Int = 0
    open val sharedCooldown: SharedCooldown = SharedCooldown.NONE

    open fun resourceCost(sim: SimIteration): Double = 0.0
    open fun resourceType(sim: SimIteration): Resource.Type = Resource.Type.MANA

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal open fun sharedState(type: SharedCooldown, sim: SimIteration): State {
        // Create state object if it does not exist, and return it
        val state = sim.sharedAbilityState[type.toString()] ?: stateFactory()
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

    open fun beforeCast(sim: SimIteration) {
        // Spend the appropriate resource
        val resourceCost = this.resourceCost(sim).toInt()
        val resourceType = this.resourceType(sim)
        if (resourceCost != 0) {
            sim.subtractResource(resourceCost, resourceType, this)
        }
    }

    abstract fun cast(sim: SimIteration)
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

    open fun castTimeMs(sim: SimIteration): Int = 0
    open fun buffs(sim: SimIteration): List<Buff> = listOf()
}
