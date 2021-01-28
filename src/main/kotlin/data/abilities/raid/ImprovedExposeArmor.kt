package data.abilities.raid

import character.*
import sim.SimIteration

class ImprovedExposeArmor : Ability() {
    companion object {
        const val name = "Improved Expose Armor"
    }

    override val id: Int = 26866
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

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
                armor = (2050 * 1.5).toInt()
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addDebuff(debuff)
    }

    override val baseCastTimeMs: Int = 0
}
