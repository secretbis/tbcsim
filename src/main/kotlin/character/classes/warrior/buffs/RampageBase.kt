package character.classes.warrior.buffs

import character.Ability
import character.Buff
import character.Proc
import character.classes.warrior.Warrior
import data.model.Item
import sim.Event
import sim.SimIteration

class RampageBase : Buff() {
    override val name: String = "Rampage (base)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    // This is just a marker to tell the rotation that we can cast Rampage
    val canUseBuff = object : Buff() {
        override val name: String = Warrior.rampageFlagBuffName
        override val durationMs: Int = 3000 // TODO: 1 server tick?  Needs confirmation
        override val hidden: Boolean = true
    }

    // On crit, proc the buff that lets us use Rampage
    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_CRIT
        )
        override val type: Type = Type.STATIC

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(canUseBuff)
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
}
