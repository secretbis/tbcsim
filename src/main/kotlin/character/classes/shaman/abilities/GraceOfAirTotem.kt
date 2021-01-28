package character.classes.shaman.abilities

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import character.classes.shaman.talents.EnhancingTotems
import sim.SimIteration

class GraceOfAirTotem : Ability() {
    companion object {
        const val name = "Grace of Air Totem"
    }

    override val id: Int = 25359
    override val name: String = Companion.name

    override fun available(sim: SimIteration): Boolean {
        return true
    }

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 120000
        override val mutex: List<Mutex> = listOf(Mutex.AIR_TOTEM)

        val baseAgi = 77.0
        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            val etTalent = sim.subject.klass.talents[EnhancingTotems.name] as EnhancingTotems?
            val multiplier = 1.0 * (etTalent?.graceOfAirTotemMultiplier() ?: 1.0)
            return stats.add(Stats(agility = (baseAgi * multiplier).toInt()))
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf()
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
    override fun gcdMs(sim: SimIteration): Int = sim.subject.totemGcd().toInt()
}
