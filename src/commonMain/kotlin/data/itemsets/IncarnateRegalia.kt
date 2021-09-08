package data.itemsets

import character.Buff
import data.model.ItemSet

class IncarnateRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Incarnate Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Incarnate Regalia (4 set)"
    }
    override val id: Int = 664

    // https://tbc.wowhead.com/spell=37570/improved-shadowfiend
    // Shadowfiend lasts 3 seconds longer for the simulation
    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    // https://tbc.wowhead.com/spell=37571/improved-mind-flay-and-smite
    // Increases Mind Flay periodic damage and Smite damage by 5%
    // TODO: Add to Smite spell when implemented
    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
