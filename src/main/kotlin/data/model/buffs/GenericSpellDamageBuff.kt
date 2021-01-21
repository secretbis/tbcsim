package data.model.buffs

import character.*
import sim.SimIteration

class GenericSpellDamageBuff(val spellDamage: Int) : Buff() {
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(spellDamage = spellDamage))
    }

    override val procs: List<Proc> = listOf()
}
