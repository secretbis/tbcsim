package data.buffs

import character.*
import sim.SimIteration

class GenericFireDamageBuff(val fireDamage: Int) : Buff() {
    override val name: String = "Fire Damage $fireDamage"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(fireDamage = fireDamage))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
