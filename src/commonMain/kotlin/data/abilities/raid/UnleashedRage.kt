package data.abilities.raid

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class UnleashedRage : Ability() {
    companion object {
        const val name = "Unleashed Rage"
    }

    override val id: Int = 25359
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = "Unleashed Rage"
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        // Assume uptime of about 90%
        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                attackPowerMultiplier = 0.10 * 0.90
            )
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
