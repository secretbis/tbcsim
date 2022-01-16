package data.abilities.generic

import character.Ability
import character.Buff
import character.Mutex
import character.Stats
import sim.SimParticipant

class SpicyCrawdad : Ability() {
    companion object {
        const val name = "Spicy Crawdad"
    }

    override val id: Int = 27667
    override val name: String = Companion.name
    override val icon: String = "inv_misc_fish_16.jpg"
    override fun gcdMs(sp: SimParticipant): Int = 0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "inv_misc_fish_16.jpg"
        override val durationMs: Int = 30 * 60 * 1000
        override val mutex: List<Mutex> = listOf(Mutex.FOOD)

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(stamina = 30, spirit = 20)
        }
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
