package data.buffs

import character.*
import data.Constants
import mechanics.Spell
import data.model.Item
import sim.Event
import sim.EventType
import sim.SimParticipant

class RomulosPoisonVial : Buff() {


    override val name: String = "Romulo's Poison Vial (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val natureAbility = object : Ability() {
        override val id: Int = 34586
        override fun gcdMs(sp: SimParticipant): Int = 0
        override val name: String = "Romulo's Poison Vial"

        val school = Constants.DamageType.NATURE
        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRoll(sp, 222.0, 332.0, school, 0.0)
            //TODO: check if the damage proc scales with increased nature debuffs e.g. stormstrike and check if it consumes SS
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


    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE,
            Trigger.RANGED_AUTO_HIT,
            Trigger.RANGED_AUTO_CRIT,
            Trigger.RANGED_WHITE_HIT,
            Trigger.RANGED_WHITE_CRIT,
            Trigger.RANGED_YELLOW_HIT,
            Trigger.RANGED_YELLOW_CRIT,
            Trigger.RANGED_BLOCK,
        )
        override val type: Type = Type.PPM
        override val ppm: Double = 2.0
        //TODO: test proc rate on live, value based on wowhead comments with a sample size of 1k

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            natureAbility.cast(sp)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
