package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class Netherblade : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Netherblade (2 set)"
        const val FOUR_SET_BUFF_NAME = "Netherblade (4 set)"

        fun twoSetSnDDurationIncreaseMs(): Int {
            return 3000
        }
    }

    override val id: Int = 621

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val icon: String = "inv_pants_plate_05.jpg"
        override val durationMs: Int = -1
    }

    val fourSetAbility = object : Ability() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_pants_plate_05.jpg"
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_pants_plate_05.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.ROGUE_CAST_FINISHER
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 15.0

            // TODO: does this also happen on misses/dodges/parry etc? otherwise we have to check here for a hit
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(1, Resource.Type.COMBO_POINT, fourSetAbility)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
