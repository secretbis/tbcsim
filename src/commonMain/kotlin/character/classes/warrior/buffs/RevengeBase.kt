package character.classes.warrior.buffs

import character.Ability
import character.Buff
import character.Proc
import data.model.Item
import sim.Event
import sim.SimParticipant

class RevengeBase : Buff() {
    companion object {
        // This is just a marker to tell the rotation that we can cast Revenge
        val revengeFlagBuff = object : Buff() {
            override val name: String = "Revenge (available)"
            override val icon: String = "ability_warrior_revenge.jpg"
            override val durationMs: Int = 3000 // TODO: 1 server tick?  Needs confirmation
            override val hidden: Boolean = true
        }
    }
    override val name: String = "Revenge (base)"
    override val icon: String = "ability_warrior_revenge.jpg"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // On crit, proc the buff that lets us use Revenge
    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.INCOMING_MELEE_BLOCK,
            Trigger.INCOMING_MELEE_DODGE,
            Trigger.INCOMING_MELEE_PARRY
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(revengeFlagBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
