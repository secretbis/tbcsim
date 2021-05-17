package data.buffs

import character.*
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.SimParticipant

class Revenger(val sourceItem: Item) : ItemBuff(listOf(sourceItem)) {
    override val name: String = "Revenger (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

        val shadowAbility = object : Ability() {
            override val id: Int = 34107
            override val name: String = "Revenger (Shadow)"
            override fun gcdMs(sp: SimParticipant): Int = 0

            val school = Constants.DamageType.SHADOW
            override fun cast(sp: SimParticipant) {
                val damageRoll = Spell.baseDamageRoll(sp, 105.00, 125.0, school, 1.0)
                //Has 100% Spell Damage scaling (from the wow Armaments Discord)
                //TODO: check if the damage proc scales with increased shadow damage debuffs e.g. shadow weaving
                val result = Spell.attackRoll(sp, damageRoll, school)

                val damageEvent = Event(
                    eventType = Event.Type.DAMAGE,
                    damageType = school,
                    abilityName = name,
                    amount = result.first,
                    result = result.second,
                )
                sp.logEvent(damageEvent)
            }
        }

        val shadowProc = object : ItemProc(listOf(sourceItem)) {
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
            override val ppm: Double = 1.0
            //proc chance based on wowhead comments (approximation)
            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                shadowAbility.cast(sp)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(shadowProc)
}
