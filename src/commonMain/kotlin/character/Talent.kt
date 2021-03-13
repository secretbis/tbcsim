package character

import sim.SimIteration
import sim.SimParticipant

abstract class Talent(private var _currentRank: Int) {
    abstract val name: String
    abstract val maxRank: Int

    // Ensure we can't go above max rank
    val currentRank: Int
        get() {
            return if(_currentRank > maxRank) {
                maxRank
            } else _currentRank
        }

    open fun buffs(sp: SimParticipant): List<Buff> = listOf()
}
