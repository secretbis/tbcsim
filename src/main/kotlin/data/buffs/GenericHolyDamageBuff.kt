package data.buffs

import character.*
import sim.SimIteration

class GenericHolyDamageBuff(val holyDamage: Int) : Buff() {
    override val name: String = "Holy Damage $holyDamage"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(holyDamage = holyDamage))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
