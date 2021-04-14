package data.abilities.generic

import character.*
import sim.SimParticipant

class ElixirOfMajorAgility : Ability() {
    companion object {
        const val name = "Elixir of Major Agility"
    }

    override val id: Int = 22831
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Elixir of Major Agility"
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(agility = 35, meleeCritRating = 20.0, rangedCritRating = 20.0)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
