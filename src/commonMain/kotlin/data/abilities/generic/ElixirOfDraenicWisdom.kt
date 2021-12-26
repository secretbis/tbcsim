package data.abilities.generic

import character.*
import sim.SimParticipant

class ElixirOfDraenicWisdom : Ability() {
    companion object {
        const val name = "Elixir of Draenic Wisdom"
    }

    override val id: Int = 32067
    override val name: String = Companion.name
    override val icon: String = "inv_potion_155.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Elixir of Draenic Wisdom"
        override val icon: String = "inv_potion_155.jpg"
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.GUARDIAN_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(intellect = 30, spirit = 30)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
