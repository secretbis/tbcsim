package data.abilities.raid

import character.Ability
import character.Buff
import character.Proc
import data.Constants
import data.model.Item
import mechanics.Spell
import sim.Event
import sim.EventType
import sim.SimParticipant
import kotlin.js.JsExport

@JsExport
class Thorns : Ability() {
    companion object {
        const val name = "Thorns"
        const val icon = "spell_nature_thorns.jpg"
    }

    override val id: Int = 26992
    override val name: String = Companion.name
    override val icon: String = Companion.icon

    val buff = object : Buff() {
        override val id: Int = 26992
        override val name: String = Companion.name
        override val icon: String = Companion.icon
        override val durationMs: Int = -1

        val thornsAbility = object : Ability() {
            override val name: String = Companion.name
            override val icon: String = Companion.icon
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.INCOMING_MELEE_HIT,
                Trigger.INCOMING_MELEE_CRIT,
                Trigger.INCOMING_MELEE_CRUSH,
                Trigger.INCOMING_MELEE_BLOCK
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                val result = Spell.attackRoll(sp, 25.0, Constants.DamageType.NATURE, true, canCrit = false)
                sp.logEvent(
                    Event(
                        eventType = EventType.DAMAGE,
                        damageType = Constants.DamageType.NATURE,
                        amount = result.first,
                        ability = thornsAbility,
                        result = result.second
                    )
                )
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun cast(sp: SimParticipant) {
        sp.addBuff(buff)
    }
}
