package data.model.buffs

import character.*
import sim.SimIteration

class GenericAttackPowerBuff(val attackPower: Int) : Buff() {
    override var appliedAtMs: Int = 0
    override val durationMs: Int = Int.MAX_VALUE
    override val statModType: ModType = ModType.FLAT
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        stats.attackPower = stats.attackPower + attackPower
        stats.rangedAttackPower = stats.rangedAttackPower + attackPower
        return stats
    }

    override val procs: List<Proc> = listOf()
}
