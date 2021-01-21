package data.model.buffs

import character.*
import sim.SimIteration

class GenericSpellHealingBuff(val spellHealing: Int) : Buff() {
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(spellHealing = spellHealing))
    }

    override val procs: List<Proc> = listOf()
}
