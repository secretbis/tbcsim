package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class NetherstrikeArmor : ItemSet() {
    override val id: Int = 617

    val threeBuff = object : Buff() {
        override val name: String = "Netherstrike Armor (3 set)"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamage = 23)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
