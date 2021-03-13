package character.classes.warrior.buffs

import character.Ability
import character.Buff
import character.Proc
import data.model.Item
import sim.Event
import sim.SimParticipant

class RampageBase : Buff() {
    companion object {
        // This is just a marker to tell the rotation that we can cast Rampage
        val rampageFlagBuff = object : Buff() {
            override val name: String = "Rampage (available)"
            override val durationMs: Int = 3000 // TODO: 1 server tick?  Needs confirmation
            override val hidden: Boolean = true
        }
    }
    override val name: String = "Rampage (base)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // On crit, proc the buff that lets us use Rampage
    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(rampageFlagBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
}
