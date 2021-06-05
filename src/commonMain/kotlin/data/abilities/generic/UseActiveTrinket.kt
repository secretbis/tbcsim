package data.abilities.generic

import character.Ability
import sim.SimIteration
import sim.SimParticipant

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
    override fun gcdMs(sp: SimParticipant): Int = 0
    override val sharedCooldown: SharedCooldown = SharedCooldown.ACTIVE_TRINKET

    private fun getActiveTrinkets(sp: SimParticipant): List<Ability> {
        return sp.buffs.values.mapNotNull { it.activeTrinketAbility(sp) }
    }

    override fun available(sp: SimParticipant): Boolean {
        return getActiveTrinkets(sp).isNotEmpty() && super.available(sp)
    }

    override fun cooldownMs(sp: SimParticipant): Int {
        val state = state(sp) as TrinketState
        return state.lastTrinketUsedDurationMs
    }

    override fun cast(sp: SimParticipant) {
        val availableTrinkets = getActiveTrinkets(sp)

        // The user can specify a "name" option to select a trinket.  Otherwise, just pick one.
        val preferredName = sp.castingRule?.options?.name
        val trinket = if(preferredName == null) {
            availableTrinkets.find { it.available(sp) }
        } else {
            availableTrinkets.find { it.name == preferredName && it.available(sp) }
        }

        if(trinket != null) {
            // Set cooldown state according to the trinket duration
            (state(sp) as TrinketState).lastTrinketUsedDurationMs = trinket.trinketLockoutMs(sp)

            // Cast
            trinket.beforeCast(sp)
            trinket.cast(sp)
            trinket.afterCast(sp)
        }
    }
}
