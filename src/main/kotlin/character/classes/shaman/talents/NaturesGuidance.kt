package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class NaturesGuidance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Nature's Guidance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    override val buffs: List<Buff> = listOf(
        object : Buff() {
            override val durationMs: Int = -1
            override val hidden: Boolean = true

            override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                val modifier = currentRank
                val physicalHitRating = modifier * Rating.meleeHitPerPct
                val spellHitRating = modifier * Rating.spellHitPerPct
                return stats.add(Stats(
                    physicalHitRating = physicalHitRating,
                    spellHitRating = spellHitRating
                ))
            }

            override val procs: List<Proc> = listOf()
        }
    )

    override val procs: List<Proc> = listOf()
}
