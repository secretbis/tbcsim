package data.abilities.generic

import character.Ability
import character.Buff
import character.Stats
import sim.SimIteration

class CrunchySerpent : Ability() {
    companion object {
        const val name = "Crunchy Serpent"
    }

    override val id: Int = 31673
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellDamage = 23, spirit = 20)
        }
    }

    override fun cast(sim: SimIteration) {
        sim.addBuff(buff)
    }
}
