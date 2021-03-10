package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimIteration

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

        override fun modifyStats(sim: SimIteration): Stats {
            val stacks = state(sim).currentStacks
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
            Trigger.MELEE_GLANCE
        )
        override val type: Type = Type.STATIC

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(apBuff)
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(apProc)
}
