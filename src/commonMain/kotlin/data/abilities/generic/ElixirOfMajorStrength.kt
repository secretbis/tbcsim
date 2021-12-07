package data.abilities.generic

import character.*
import sim.SimParticipant

class ElixirOfMajorStrength : Ability() {
    companion object {
        const val name = "Elixir of Major Strength"
    }

    override val id: Int = 22831
    override val name: String = Companion.name
    override val icon: String = "inv_potion_147.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Elixir of Major Strength"
        override val icon: String = "inv_potion_147.jpg"
        override val durationMs: Int = 60 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.BATTLE_ELIXIR)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(strength = 35)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
