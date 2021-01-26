package data.buffs

import character.*
import character.classes.druid.Druid
import sim.SimIteration

class GenericFeralAttackPowerBuff(val attackPower: Int) : Buff() {
    override val name: String = "Feral Attack Power $attackPower"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        // TODO: Is this good enough, or should this be another stat?
        if(sim.subject.klass is Druid) {
            return stats.add(Stats(attackPower = attackPower))
        }

        return stats
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf()
}
