package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class WindhawkArmor : ItemSet() {
    override val id: Int = 618

    val threeBuff = object : Buff() {
        override val name: String = "Windhawk Armor (3 set)"
        override val icon: String = "inv_chest_leather_01.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaPer5Seconds = 8)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
