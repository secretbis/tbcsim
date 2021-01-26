package data.buffs

import character.*
import sim.SimIteration

class GenericNatureDamageBuff(val natureDamage: Int) : Buff() {
    override val name: String = "Nature Damage"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(natureDamage = natureDamage))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
