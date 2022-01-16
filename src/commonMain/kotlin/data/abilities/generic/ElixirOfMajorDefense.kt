package data.abilities.generic

import character.*
import sim.SimParticipant

class ElixirOfMajorDefense : Ability() {
    companion object {
        const val name = "Elixir of Major Defense"
    }

    override val id: Int = 22834
    override val name: String = Companion.name
    override val icon: String = "inv_potion_122.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_potion_122.jpg"
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(armor = 550)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
