package data.buffs.generic

import character.Ability
import character.Buff
import character.Proc
import data.model.Item
import sim.Event
import sim.SimParticipant

class ParryHaste : Buff() {
    companion object {
        const val name = "Parry Haste"
    }

    override val name: String = Companion.name
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_PARRY
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.mhAutoAttack?.parryHaste(sp)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}