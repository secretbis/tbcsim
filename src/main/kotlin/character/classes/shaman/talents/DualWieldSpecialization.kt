package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class DualWieldSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dual Wield Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            // 2% hit per rank
            val modifier = currentRank
            val physicalHitRating = modifier * 2 * Rating.meleeHitPerPct

            // Only when dual wielding
            return if(sim.subject.isDualWielding()) {
                stats.add(
                    Stats(
                        physicalHitRating = physicalHitRating
                    )
                )
            } else stats
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
