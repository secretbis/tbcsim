package character

import data.Constants
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
abstract class Ability {
    open class State {
        var cooldownStartMs: Int = -1
    }

    enum class SharedCooldown {
        NONE,
        SHAMAN_SHOCK,
        POTION,
        RUNE_OR_MANA_GEM,
        ACTIVE_TRINKET,
        WARRIOR_STANCE
    }

    open val id: Int = -1
    abstract val name: String
    open val icon: String = Constants.UNKNOWN_ICON

    open fun gcdMs(sp: SimParticipant): Int = 0
    open val castableOnGcd = false

    open fun cooldownMs(sp: SimParticipant): Int = 0
    open val sharedCooldown: SharedCooldown = SharedCooldown.NONE
    open fun trinketLockoutMs(sp: SimParticipant): Int = 0

    open fun resourceCost(sp: SimParticipant): Double = 0.0
    open fun resourceType(sp: SimParticipant): Resource.Type = Resource.Type.MANA

    // Buff implementations can implement their own state containers
    internal open fun stateFactory(): State {
        return State()
    }

    internal open fun sharedState(type: SharedCooldown, sp: SimParticipant): State {
        // Create state object if it does not exist, and return it
        // TODO: Shared state with factory overrides can conflict - needs to be owned elsewhere
        val state = sp.sharedAbilityState[type.toString()] ?: stateFactory()
        sp.sharedAbilityState[type.toString()] = state
        return state
    }

    internal fun state(sp: SimParticipant): State {
        // Create state object if it does not exist, and return it
        var state = sp.abilityState[name]
        if(state == null) {
            state = stateFactory()
            sp.abilityState[name] = state
        }
        return state
    }

    open fun currentCooldownMs(sp: SimParticipant): Int {
        val state = if(sharedCooldown == SharedCooldown.NONE) {
            state(sp)
        } else {
            sharedState(sharedCooldown, sp)
        }

        return if(state.cooldownStartMs == -1) {
            -1
        } else {
             state.cooldownStartMs + cooldownMs(sp) - sp.sim.elapsedTimeMs
        }
    }

    open fun available(sp: SimParticipant): Boolean {
        return currentCooldownMs(sp) <= 0
    }

    open fun beforeCast(sp: SimParticipant) {
        // Spend the appropriate resource
        val resourceCost = this.resourceCost(sp).toInt()
        val resourceType = this.resourceType(sp)
        if (resourceCost != 0) {
            sp.subtractResource(resourceCost, resourceType, this)
        }
    }

    open fun cast(sp: SimParticipant) {
        // Noop
    }

    open fun afterCast(sp: SimParticipant) {
        // Store individual cooldown state
        val state = state(sp)
        state.cooldownStartMs = sp.sim.elapsedTimeMs

        // Store shared cooldown state
        if(sharedCooldown != SharedCooldown.NONE) {
            val sharedState = sharedState(sharedCooldown, sp)
            sharedState.cooldownStartMs = sp.sim.elapsedTimeMs
        }
    }

    open fun castTimeMs(sp: SimParticipant): Int = 0
    open fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
