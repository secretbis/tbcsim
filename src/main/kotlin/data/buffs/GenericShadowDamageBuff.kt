package data.buffs

import character.*
import sim.SimIteration

class GenericShadowDamageBuff(val shadowDamage: Int) : Buff() {
    override val name: String = "Shadow Damage $shadowDamage"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(shadowDamage = shadowDamage))
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
