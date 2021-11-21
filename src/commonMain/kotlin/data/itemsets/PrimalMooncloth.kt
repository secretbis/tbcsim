package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import mechanics.General
import sim.SimParticipant

class PrimalMooncloth : ItemSet() {
    override val id: Int = 554

    // 5% regen while casting
    val threeBuff = object : Buff() {
        override val name: String = "Primal Mooncloth (3 set)"
        override val icon: String = "inv_chest_cloth_04.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            val spiritMp5 = General.mp5FromSpiritNotCasting(sp)
            return Stats(manaPer5Seconds = (spiritMp5 * 0.05).toInt())
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
