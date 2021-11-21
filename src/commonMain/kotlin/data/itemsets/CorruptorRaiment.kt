package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class CorruptorRaiment : ItemSet() {
    companion object {
        const val FOUR_SET_BUFF_NAME = "Corruptor Raiment (4 set)"
        const val FOUR_SET_CORRUPTION_BUFF_NAME = "$FOUR_SET_BUFF_NAME (Corruption)"
        const val FOUR_SET_IMMOLATE_BUFF_NAME = "$FOUR_SET_BUFF_NAME (Immolate)"

        fun fourSetDotDamageIncreaseMultiplier(): Double {
            return 1.1
        }
    }

    override val id: Int = 646

    // The two-set is not relevant

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val icon: String = "inv_shoulder_25.jpg"
        override val durationMs: Int = -1

        val shadowDamageBuff = object : Buff() {
            override val name: String = FOUR_SET_CORRUPTION_BUFF_NAME
            // TODO: Can't find if this has any sort of duration or not
            override val durationMs: Int = -1
        }

        val shadowProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.WARLOCK_HIT_SHADOW_BOLT,
                Trigger.WARLOCK_CRIT_SHADOW_BOLT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(shadowDamageBuff)
            }
        }

        val fireDamageBuff = object : Buff() {
            override val name: String = FOUR_SET_IMMOLATE_BUFF_NAME
            override val icon: String = "inv_shoulder_25.jpg"
            // TODO: Can't find if this has any sort of duration or not
            override val durationMs: Int = -1
        }

        val fireProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.WARLOCK_HIT_INCINERATE,
                Trigger.WARLOCK_CRIT_INCINERATE
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(fireDamageBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(shadowProc, fireProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 4, fourBuff)
    )
}
