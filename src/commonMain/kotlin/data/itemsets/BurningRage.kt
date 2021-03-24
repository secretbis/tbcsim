package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class BurningRage : ItemSet() {
    override val id: Int = 566

    val twoBuff = object : Buff() {
        override val name: String = "Burning Rage (2 set)"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = 20.0)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff)
    )
}
