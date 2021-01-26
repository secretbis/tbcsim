package data.buffs

import character.*
import sim.SimIteration

class GenericHitRatingBuff(val rating: Int) : Buff() {
    override val name: String = "Hit Rating $rating"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(physicalHitRating = rating.toDouble()))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
