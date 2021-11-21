package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class ImbuedNetherweave : ItemSet() {
    companion object {
        const val THREE_SET_BUFF_NAME = "Imbued Netherweave (3 set)"
    }
    override val id: Int = 556

    val threeBuff = object : Buff() {
        override val name: String = THREE_SET_BUFF_NAME
        override val icon: String = "inv_chest_cloth_12.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellCritRating = 28.0)
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
