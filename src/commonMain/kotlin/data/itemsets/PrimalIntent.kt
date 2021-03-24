package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class PrimalIntent : ItemSet() {
    override val id: Int = 619

    val threeBuff = object : Buff() {
        override val name: String = "Primal Intent (3 set)"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(attackPower = 40, rangedAttackPower = 40)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
