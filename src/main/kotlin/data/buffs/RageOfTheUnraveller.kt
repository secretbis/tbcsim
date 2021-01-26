package data.buffs

import character.*
import data.model.Item
import sim.SimIteration

class RageOfTheUnraveller : Buff() {
    override val id: Int = 33648
    override val name: String = "Rage of the Unraveller"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
        return stats
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )

        override val type: Type = Type.PPM
        override val ppm: Double = 1.0

        val buff = object : Buff() {
            override val name: String = "Rage of the Unraveller"
            override val durationMs: Int = 10000

            override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                return stats.add(Stats(attackPower = 300))
            }

            override fun procs(sim: SimIteration): List<Proc> = listOf()
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
            sim.addBuff(buff)
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
}
