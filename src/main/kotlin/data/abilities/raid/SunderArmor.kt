package data.abilities.raid

import character.*
import sim.SimIteration

class SunderArmor : Ability() {
    companion object {
        const val name = "Sunder Armor"
    }

    override val id: Int = 25225
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
            val impEaActive = sim.debuffs.find { it.name == "Improved Expose Armor" } != null
            return if(impEaActive) {
                stats
            } else {
                stats.subtract(Stats(
                    armor = 520 * 5
                ))
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addDebuff(debuff)
    }

    override val baseCastTimeMs: Int = 0
}
