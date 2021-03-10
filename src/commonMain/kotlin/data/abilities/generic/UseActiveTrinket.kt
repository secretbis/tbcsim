package data.abilities.generic

import character.Ability
import sim.SimIteration

class UseActiveTrinket : Ability() {
    companion object {
        const val name = "Use Active Trinket"
    }

    class TrinketState : State() {
        var lastTrinketUsedDurationMs: Int = 0
    }

    override fun stateFactory(): State {
        return TrinketState()
    }

    override val id: Int = -1
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0
    override val sharedCooldown: SharedCooldown = SharedCooldown.ACTIVE_TRINKET

    private fun getActiveTrinkets(sim: SimIteration): List<Ability> {
        return sim.buffs.values.mapNotNull { it.activeTrinketAbility(sim) }
    }

    override fun available(sim: SimIteration): Boolean {
        return getActiveTrinkets(sim).isNotEmpty() && super.available(sim)
    }

    override fun cooldownMs(sim: SimIteration): Int {
        val state = state(sim) as TrinketState
        return state.lastTrinketUsedDurationMs
    }

    override fun cast(sim: SimIteration) {
        val availableTrinkets = getActiveTrinkets(sim)

        // The user can specify a "name" option to select a trinket.  Otherwise, just pick one.
        val preferredName = sim.castingRule?.options?.name
        val trinket = availableTrinkets.find { it.name == preferredName } ?: availableTrinkets.firstOrNull()

        if(trinket != null && trinket.available(sim)) {
            // Set cooldown state according to the trinket duration
            (state(sim) as TrinketState).lastTrinketUsedDurationMs = trinket.trinketLockoutMs(sim)

            // Cast
            trinket.beforeCast(sim)
            trinket.cast(sim)
            trinket.afterCast(sim)
        }
    }
}
