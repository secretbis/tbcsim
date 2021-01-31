package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class ThunderingStrikes(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Thundering Strikes"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sim: SimIteration): Stats {
            val critPct = 1.0 * currentRank
            return Stats(physicalCritRating = critPct * Rating.critPerPct)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
