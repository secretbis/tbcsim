package data.buffs

import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import sim.Event
import sim.SimIteration

class DarkmoonCardCrusadeSP : Buff() {
    companion object {
        const val name = "Darkmoon Card: Crusade (SP)"
    }

    override val name: String = "${Companion.name} (static)"
    override val durationMs: Int = -1
    override val hidden: Boolean = true

    val spBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = 10000
        override val maxStacks: Int = 10

        override fun modifyStats(sim: SimIteration): Stats {
            val stacks = state(sim).currentStacks
            return Stats(spellDamage = 8 * stacks)
        }
    }

    val spProc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_HIT,
            Trigger.SPELL_CRIT,
        )
        override val type: Type = Type.STATIC

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
            sim.addBuff(spBuff)
        }
    }

    override fun procs(sim: SimIteration): List<Proc> = listOf(spProc)
}
