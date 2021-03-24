package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class CataclysmRegalia : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Cataclysm Regalia (4 set)"
    }

    override val id: Int = 635

    // The two-set is entirely useless

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHAMAN_CRIT_LIGHTNING_BOLT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 25.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(120, Resource.Type.MANA, FOUR_SET_BUFF_NAME)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
