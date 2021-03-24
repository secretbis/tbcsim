package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class ArcanoweaveVestments : ItemSet() {
    override val id: Int = 558

    val threeBuff = object : Buff() {
        override val name: String = "Arcanoweave Vestments (3 set)"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHitRating = 16.0)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
