package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class TheTwinStars : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "The Twin Stars (2 set)"
    }
    override val id: Int = 667

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_jewelry_ring_56.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(spellDamage = 15)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff)
    )
}
