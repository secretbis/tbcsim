package character.classes.shaman.debuffs

import character.Debuff
import character.Proc
import character.Stats

import sim.SimIteration

class FlameShockDot : Debuff() {
    override val name: String = "Flame Shock (DoT)"
    override val durationMs: Int = 12000

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
