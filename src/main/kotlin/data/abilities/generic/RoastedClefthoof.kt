package data.abilities.generic

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import sim.SimIteration

class RoastedClefthoof : Ability() {
    companion object {
        const val name = "Roasted Clefthoof"
    }

    override val id: Int = 27658
    override val name: String = Companion.name
    override fun gcdMs(sim: SimIteration): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(strength = 20, spirit = 20)
        }
    }

    override fun cast(sim: SimIteration, free: Boolean) {
        sim.addBuff(buff)
    }

    override val baseCastTimeMs: Int = 0
}
