package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import sim.SimIteration

class AncestralKnowledge(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Ancestral Knowledge"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    override val buffs: List<Buff> = listOf(
        object : Buff() {
            override val name: String = Companion.name
            override val durationMs: Int = -1
            override val hidden: Boolean = true

            override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                val talentRanks = sim.subject.talents[AncestralKnowledge.name]?.currentRank ?: 0

                val modifier = 1 + (0.01 * talentRanks)
                return stats.add(
                    Stats(
                        manaMultiplier = modifier
                    )
                )
            }

            override val procs: List<Proc> = listOf()
        }
    )

    override val procs: List<Proc> = listOf()
}
