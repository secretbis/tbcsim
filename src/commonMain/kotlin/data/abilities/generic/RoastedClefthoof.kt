package data.abilities.generic

import character.*
import sim.SimParticipant

class RoastedClefthoof : Ability() {
    companion object {
        const val name = "Roasted Clefthoof"
    }

    override val id: Int = 27658
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Roasted Clefthoof"
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(strength = 20, spirit = 20)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
