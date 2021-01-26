package data.buffs

import character.*
import sim.SimIteration

class GenericFrostDamageBuff(val frostDamage: Int) : Buff() {
    override val name: String = "Frost Damage $frostDamage"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(frostDamage = frostDamage))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
