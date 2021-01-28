package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import mechanics.Rating
import sim.SimIteration

class ImprovedSealOfTheCrusader : Ability() {
    companion object {
        const val name = "Improved Seal of the Crusader"
    }

    override val id: Int = 27158
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    // Always assume the raid buffer has 3/3 imp seal
    val bonusCritRating = 3.0 * Rating.critPerPct
    val buff = object : Buff() {
        override val name: String = Companion.name
        // Assume the caster is always maintaining this
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(
                physicalCritRating = bonusCritRating,
                spellCritRating = bonusCritRating
            ))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
