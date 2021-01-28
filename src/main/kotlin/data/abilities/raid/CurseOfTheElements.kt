package data.abilities.raid

import character.*
import sim.SimIteration

class CurseOfTheElements : Ability() {
    companion object {
        const val name = "Curse of the Elements"
    }

    override val id: Int = 27228
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(
                fireDamageMultiplier = 1.1,
                frostDamageMultiplier = 1.1,
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    val debuff = object : Debuff() {
        override fun tick(sim: SimIteration) {
            // Not periodic, do nothing
        }

        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.subtract(Stats(
                fireResistance = 88,
                frostResistance = 88
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
        sim.addDebuff(debuff)
    }

    override val baseCastTimeMs: Int = 0
}
