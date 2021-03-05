package data.abilities.raid

import character.*
import sim.SimIteration

class ImprovedScorch : Ability() {
    companion object {
        const val name = "Improved Scorch"
    }

    override val id: Int = 12873
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Improved Scorch"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(fireDamageMultiplier = 1.15)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
