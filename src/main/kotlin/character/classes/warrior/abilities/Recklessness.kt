package character.classes.warrior.abilities

import character.Ability
import character.Buff
import character.Stats
import mechanics.Rating
import sim.SimIteration

class Recklessness : Ability() {
    companion object {
        const val name = "Recklessness"
    }

    override val id: Int = 1719
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = sim.physicalGcd().toInt()
    override fun cooldownMs(sim: SimIteration): Int = 30 * 60 * 1000

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 15000

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(physicalCritRating = 100.0 * Rating.critPerPct)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
