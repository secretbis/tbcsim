package data.buffs

import character.*
import sim.SimIteration

class GenericBlockValueBuff(val blockValue: Int) : Buff() {
    override val name: String = "Block Value $blockValue"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(blockValue = blockValue.toDouble()))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
