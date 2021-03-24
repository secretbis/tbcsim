package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimParticipant

class DarkmoonCardCrusadeAP : Buff() {
    companion object {
        const val name = "Darkmoon Card: Crusade (AP)"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val apBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000
        override val maxStacks: Int = 20

        override fun modifyStats(sp: SimParticipant): Stats {
            val stacks = state(sp).currentStacks
            return Stats(attackPower = 6 * stacks)
        }
    }

    val apProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.MELEE_AUTO_HIT,
            Trigger.MELEE_AUTO_CRIT,
            Trigger.MELEE_WHITE_HIT,
            Trigger.MELEE_WHITE_CRIT,
            Trigger.MELEE_YELLOW_HIT,
            Trigger.MELEE_YELLOW_CRIT,
            Trigger.MELEE_BLOCK,
            Trigger.MELEE_GLANCE
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(apBuff)
        }
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(apProc)
}
