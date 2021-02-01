package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class BlessingOfWisdom : Ability() {
    companion object {
        const val name = "Blessing of Wisdom"
    }

    override val id: Int = 20217
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(manaPer5Seconds = 41)
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }
}
