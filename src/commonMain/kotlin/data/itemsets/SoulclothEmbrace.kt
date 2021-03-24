package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class SoulclothEmbrace : ItemSet() {
    companion object {
        const val THREE_SET_BUFF_NAME = "Soulcloth Embrace (3 set)"
    }
    override val id: Int = 557

    val threeBuff = object : Buff() {
        override val name: String = THREE_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHitRating = 16.0)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
