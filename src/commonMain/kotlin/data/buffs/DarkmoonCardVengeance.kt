package data.buffs

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant

class DarkmoonCardVengeance : Buff() {
    companion object {
        const val name = "Darkmoon Card: Vengeance"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true
    override val icon: String = "inv_misc_ticket_tarot_vengeance.jpg"

    val ability = object : Ability() {
        override val name: String = Companion.name
        override val icon: String = "inv_misc_ticket_tarot_vengeance.jpg"
    }

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_HIT,
            Trigger.INCOMING_MELEE_CRIT,
            Trigger.INCOMING_MELEE_CRUSH,
            Trigger.INCOMING_MELEE_BLOCK,
            Trigger.INCOMING_SPELL_HIT,
            Trigger.INCOMING_SPELL_CRIT,
        )
        override val type: Type = Type.PERCENT
        override fun percentChance(sp: SimParticipant): Double = 10.0

        val baseDamage = Pair(95.0, 115.0)
        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            val damageRoll = Spell.baseDamageRoll(sp, baseDamage.first, baseDamage.second, Constants.DamageType.HOLY, 0.001)
            val result = Spell.attackRoll(sp, damageRoll, Constants.DamageType.HOLY)

            sp.logEvent(
                Event(
                    eventType = EventType.DAMAGE,
                    damageType = Constants.DamageType.HOLY,
                    ability = ability,
                    amount = result.first,
                    result = result.second,
                )
            )
        }

    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}