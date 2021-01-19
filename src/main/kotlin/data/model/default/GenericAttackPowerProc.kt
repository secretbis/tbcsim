package data.model.default

import character.*
import sim.Sim

class GenericAttackPowerProc(attackPower: Int) : Proc() {
    override val static = true

    private val buff = object : Buff() {
        override var appliedAtMs: Int = 0
        override val durationMs: Int = Int.MAX_VALUE
        override val statModType: ModType = ModType.FLAT
        override val hidden: Boolean = true

        override fun modifyStats(sim: Sim, stats: Stats): Stats {
            stats.attackPower = stats.attackPower + attackPower
            return stats
        }

    }

    override fun proc(character: Character) {
        character.buffs.add(buff)
    }
}
