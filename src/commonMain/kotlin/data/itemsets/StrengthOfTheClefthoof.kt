package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class StrengthOfTheClefthoof : ItemSet() {
    companion object {
        const val THREE_SET_BUFF_NAME = "Strength of the Clefthoof (3 set)"
    }
    override val id: Int = 574

    val threeBuff = object : Buff() {
        override val name: String = THREE_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(strength = 20)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
