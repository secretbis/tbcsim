package data.buffs

import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class GenericManaRegenBuff(val regen: Int): Buff() {
    override val name: String = "Mana Regen $regen/5s"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(manaPer5Seconds = regen))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
