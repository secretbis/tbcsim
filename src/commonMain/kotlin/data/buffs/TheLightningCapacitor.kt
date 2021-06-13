package data.buffs

import character.*
import data.Constants
import mechanics.Spell
import data.model.Item
import sim.Event
import sim.EventType
import sim.SimParticipant

class TheLightningCapacitor : Buff() {


    override val name: String = "The Lightning Capacitor (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val natureAbility = object : Ability() {
        override val id: Int = 37657
        override fun gcdMs(sp: SimParticipant): Int = 0
        override val name: String = "The Lightning Capacitor"

        val school = Constants.DamageType.NATURE
        override fun cast(sp: SimParticipant) {
            val damageRoll = Spell.baseDamageRoll(sp, 694.0, 806.0, school, 0.0)
            //TODO: check if the damage proc scales with increased nature debuffs e.g. stormstrike and check if it consumes SS
            sp.consumeBuff(stackBuff)
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

    val stackBuff = object : Buff() {
        override val name: String = "Electric Charge"
        override val durationMs: Int = -1
        override val maxStacks: Int = 3
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_CRIT,
        )
        override val type: Type = Type.STATIC
        override fun cooldownMs(sp: SimParticipant): Int = 2500
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            val state = stackBuff.state(sp)
             if(state.currentStacks < 2) {
                 sp.addBuff(stackBuff)
             } else {
                natureAbility.cast(sp)
             }
        }

    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
