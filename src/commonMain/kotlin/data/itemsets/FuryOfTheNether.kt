package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class FuryOfTheNether : ItemSet() {
    companion object {
        const val THREE_SET_BUFF_NAME = "Fury of the Nether (3 set)"
    }
    override val id: Int = 576

    val threeBuff = object : Buff() {
        override val name: String = THREE_SET_BUFF_NAME
        override val icon: String = "inv_boots_chain_06.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(intellect = 20)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
