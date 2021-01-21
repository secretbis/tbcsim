package data.model.buffs

import character.*
import sim.SimIteration

class GenericAttackPowerBuff(val attackPower: Int) : Buff() {
    override val name: String = "Attack Power"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats.add(Stats(attackPower = attackPower, rangedAttackPower = attackPower))
    }

    override val procs: List<Proc> = listOf()
}
