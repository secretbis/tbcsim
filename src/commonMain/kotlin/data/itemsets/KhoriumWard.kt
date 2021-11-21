package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class KhoriumWard : ItemSet() {
    override val id: Int = 565

    val threeBuff = object : Buff() {
        override val name: String = "Khorium Ward (3 set)"
        override val icon: String = "inv_belt_11.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellHealing = 55, spellDamage = 19)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
