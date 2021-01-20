package data.model.buffs

import character.*
import sim.Sim

class GenericRangedAttackPowerBuff(val rangedAttackPower: Int) : Buff() {
    override var appliedAtMs: Int = 0
    override val durationMs: Int = Int.MAX_VALUE
    override val statModType: ModType = ModType.FLAT
    override val hidden: Boolean = true

    override fun modifyStats(sim: Sim, stats: Stats): Stats {
        stats.rangedAttackPower = stats.rangedAttackPower + rangedAttackPower
        return stats
    }

    override val procs: List<Proc> = listOf()
}
