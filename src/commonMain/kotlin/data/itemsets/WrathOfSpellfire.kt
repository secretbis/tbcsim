package data.itemsets

import character.Buff
import character.Stats
import data.model.ItemSet
import sim.SimParticipant

class WrathOfSpellfire : ItemSet() {
    companion object {
        const val THREE_SET_BUFF_NAME = "Wrath of Spellfire (3 set)"
    }
    override val id: Int = 552

    val threeBuff = object : Buff() {
        override val name: String = THREE_SET_BUFF_NAME
        override val icon: String = "inv_chest_cloth_02.jpg"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(spellDamage = (sp.intellect() * 0.07).toInt())
        }
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 3, threeBuff)
    )
}
