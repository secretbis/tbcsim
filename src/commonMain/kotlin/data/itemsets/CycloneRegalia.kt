package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Resource
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant
import kotlin.math.min

class CycloneRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Cyclone Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Cyclone Regalia (4 set)"

        fun twoSetWrathOfAirBonus(): Int {
            return 20
        }
    }

    override val id: Int = 632

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    fun fourSetConsumeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHAMAN_CAST_LIGHTNING_BOLT,
                Trigger.SHAMAN_CAST_CHAIN_LIGHTNING,
                Trigger.SHAMAN_CAST_SHOCK
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)

                // Refund the cost
                if(ability != null) {
                    // Choose the lower of the spell cost, or the 270 set reduction
                    val refund = min(ability.resourceCost(sp), 270.0).toInt()
                    sp.addResource(refund, Resource.Type.MANA, FOUR_SET_BUFF_NAME)
                }
            }
        }
    }

    val fourSetCostReductionBuff = object : Buff() {
        override val name: String = "$FOUR_SET_BUFF_NAME (cost reduction)"
        override val durationMs: Int = 15000

        val consumeProc = fourSetConsumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val critProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 11.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(fourSetCostReductionBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(critProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
