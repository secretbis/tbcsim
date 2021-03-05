package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class StrengthOfEarthTotem : Ability() {
    companion object {
        const val name = "Strength of Earth Totem"
    }

    override val id: Int = 25528
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Strength of Earth Totem"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume 100% uptime and that the caster has Enhancing Totems
        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                strength = (86.0 * 1.15).toInt()
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
