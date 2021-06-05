package data.itemsets

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import data.model.ItemSet
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant

class TheFistsOfFury : ItemSet() {
    companion object {
        const val TWO_SET_BUFF_NAME = "The Fists of Fury (2 set)"
    }
    override val id: Int = 719

    val twoBuff = object : Buff() {
        override val name: String = TWO_SET_BUFF_NAME
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val fireAbility = object : Ability() {
            override val id: Int = 41989
            override val name: String = "$TWO_SET_BUFF_NAME (Fire)"
            override fun gcdMs(sp: SimParticipant): Int = 0

            val school = Constants.DamageType.FIRE
            override fun cast(sp: SimParticipant) {
                val damageRoll = Spell.baseDamageRoll(sp, 100.0, 150.0, school, 0.0)
                val result = Spell.attackRoll(sp, damageRoll, school)

                val damageEvent = Event(
                    eventType = EventType.DAMAGE,
                    damageType = school,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                )
                sp.logEvent(damageEvent)
            }
        }

        val fireProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.MELEE_AUTO_HIT,
                Trigger.MELEE_AUTO_CRIT,
                Trigger.MELEE_WHITE_HIT,
                Trigger.MELEE_WHITE_CRIT,
                Trigger.MELEE_YELLOW_HIT,
                Trigger.MELEE_YELLOW_CRIT,
                Trigger.MELEE_GLANCE,
                Trigger.MELEE_BLOCK
            )
            override val type: Type = Type.PPM
            override val ppm: Double = 0.5

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                fireAbility.cast(sp)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(fireProc)
    }

    override val bonuses: List<Bonus> = listOf(
        Bonus(id, 2, twoBuff)
    )
}
