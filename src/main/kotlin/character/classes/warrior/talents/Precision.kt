package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class Precision(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Precision"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "Precision"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalHitRating = currentRank * Rating.meleeHitPerPct)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
