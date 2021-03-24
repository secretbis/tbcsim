package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class TidefuryRaiment : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Tidefury Raiment (2 set)"
        const val FOUR_SET_BUFF_NAME = "Tidefury Raiment (4 set)"
    }
    override val id: Int = 630

    // TODO: Chain Lightning should check this buff once multi-target fights exist
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            // TODO: If Water Shield proccing is ever a thing, this should proc off that
            if(sp.buffs["Water Shield"] != null) {
                return Stats(manaPer5Seconds = 3)
            }

            return Stats()
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
