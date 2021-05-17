package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import data.model.ItemSet
import sim.Event
import sim.SimParticipant

class VoidheartRaiment : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "Voidheart Raiment (2 set)"
        const val FOUR_SET_BUFF_NAME = "Voidheart Raiment (4 set)"

        fun fourSetIncreasedDotDurationMs(): Int {
            return 3000
        }
    }
    override val id: Int = 645

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1

        val shadowDamageBuff = object : Buff() {
            override val name: String = "$TWO_SET_BUFF_NAME (Shadow)"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(shadowDamage = 135)
            }
        }

        val shadowProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHADOW_DAMAGE_NON_PERIODIC
            )
            // TODO: Proc rate unknown, assuming 1PPM for now
            override val type: Type = Type.PPM
            override val ppm: Double = 1.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(shadowDamageBuff)
            }
        }

        val fireDamageBuff = object : Buff() {
            override val name: String = "$TWO_SET_BUFF_NAME (Fire)"
            override val durationMs: Int = 15000

            override fun modifyStats(sp: SimParticipant): Stats? {
                return Stats(fireDamage = 135)
            }
        }

        val fireProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.FIRE_DAMAGE_NON_PERIODIC
            )
            // TODO: Proc rate unknown, assuming 1PPM for now
            override val type: Type = Type.PPM
            override val ppm: Double = 1.0

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(fireDamageBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(shadowProc, fireProc)
    }

    val fourBuff = object : Buff() {
        override val name: String = FOUR_SET_BUFF_NAME
        override val durationMs: Int = -1
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff),
        Bonus(id, 4, fourBuff)
    )
}
