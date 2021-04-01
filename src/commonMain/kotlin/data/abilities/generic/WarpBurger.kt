package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class WarpBurger : Ability() {
    companion object {
        const val name = "Warp Burger"
    }

    override val id: Int = 27659
    override val name: String = Companion.name
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = "Warp Burger"
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(agility = 20, spirit = 20)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
