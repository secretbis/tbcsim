package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class LatrosFlurry : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Latro's Flurry(2 set)"
    }
    override val id: Int = 737

    val threeBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(attackPower = 30)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
