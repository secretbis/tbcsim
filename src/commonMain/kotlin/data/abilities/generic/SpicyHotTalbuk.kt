package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class SpicyHotTalbuk : Ability() {
    companion object {
        const val name = "Spicy Hot Talbuk"
    }

    override val id: Int = 33872
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Spicy Hot Talbuk"
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = 20.0, spirit = 20)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
