package data.abilities.raid

import character.*
import sim.SimIteration

class Misery : Ability() {
    companion object {
        const val name = "Misery"
    }

    override val id: Int = 33195
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellDamageMultiplier = 1.05)
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
