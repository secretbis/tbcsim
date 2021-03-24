package data.itemsets

import character.*
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant
import kotlin.math.min

class AvatarRegalia : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Avatar Regalia (2 set)"
        const val FOUR_SET_BUFF_NAME = "Avatar Regalia (4 set)"
    }

    override val id: Int = 666

    fun twoSetConsumeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)

                // Refund the cost
                if(ability != null) {
                    // Choose the lower of the spell cost, or the 150 set reduction
                    val refund = min(ability.resourceCost(sp), 150.0).toInt()
                    sp.addResource(refund, Resource.Type.MANA, TWO_SET_BUFF_NAME)
                }
            }
        }
    }

    val twoSetCostReductionBuff = object : Buff() {
        override val name: String = "$TWO_SET_BUFF_NAME (cost reduction)"
        override val durationMs: Int = -1

        val consumeProc = twoSetConsumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
    }

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 6.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(twoSetCostReductionBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    fun fourSetConsumeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val fourSetSpellDamageBuff = object : Buff() {
        override val name: String = "$FOUR_SET_BUFF_NAME (Spell Damage)"
        override val durationMs: Int = 15000

        override fun modifyStats(sp: SimParticipant): Stats? {
            return Stats(spellDamage = 100)
        }

        val consumeProc = fourSetConsumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val swpProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.PRIEST_TICK_SHADOW_WORD_PAIN
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 40.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(fourSetSpellDamageBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(swpProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
