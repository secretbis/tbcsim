package data.abilities.raid

import character.*
import sim.SimIteration

class ShadowWeaving : Ability() {
    companion object {
        const val name = "Shadow Weaving"
    }

    override val id: Int = 33195
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this at max stacks
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(shadowDamageMultiplier = 1.1)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
