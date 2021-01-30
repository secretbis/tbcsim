package data.abilities.raid

import character.*
import sim.SimIteration

// Since this is a deep Balance talent, it's not quite reasonable to assume 3/3 Improved
class FaerieFire : Ability() {
    companion object {
        const val name = "Faerie Fire"
    }

    override val id: Int = 26993
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val debuff = object : Debuff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(
                armor = -610
            )
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addDebuff(debuff)
    }

    override val baseCastTimeMs: Int = 0
}
