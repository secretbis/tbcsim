package data.abilities.generic

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class ElixirOfMajorAgility : Ability() {
    companion object {
        const val name = "Elixir of Major Agility"
    }

    override val id: Int = 22831
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(agility = 35, physicalCritRating = 20.0)
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
