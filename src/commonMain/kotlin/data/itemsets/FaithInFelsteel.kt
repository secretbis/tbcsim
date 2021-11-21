package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class FaithInFelsteel : ItemSet() {
    override val id: Int = 569

    val threeBuff = object : Buff() {
        override val name: String = "Faith in Felsteel (3 set)"
        override val icon: String = "inv_helmet_22.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(strength = 25)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
