package data.buffs

import character.*
import sim.SimIteration

class GenericSpellPenBuff(val spellPen: Int) : Buff() {
    override val name: String = "Spell Penetration $spellPen"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(spellPen = spellPen))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
