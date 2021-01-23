package data.model.default

import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class UnknownBuff : Buff() {
    override val name: String = "Unknown"
    override val durationMs: Int = 0

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
