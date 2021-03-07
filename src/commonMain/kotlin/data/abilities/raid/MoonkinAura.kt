package data.abilities.raid

import character.*
import mechanics.Rating
import sim.SimIteration

class MoonkinAura : Ability() {
    companion object {
        const val name = "Moonkin Aura"
    }

    override val id: Int = 24907
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellCritRating = Rating.critPerPct * 5.0)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
