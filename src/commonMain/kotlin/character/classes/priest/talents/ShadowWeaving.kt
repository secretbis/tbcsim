package character.classes.priest.talents

import character.*
import character.classes.priest.buffs.ShadowWeavingBuff
import data.Constants
import data.model.Item
import sim.Event
import sim.EventResult
import sim.EventType
import sim.SimParticipant

import kotlin.random.Random

class ShadowWeaving(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Weaving"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_HIT,
                Trigger.SPELL_CRIT,
            )
            override val type: Type = Type.PERCENT

            override fun percentChance(sp: SimParticipant): Double = 100.0 * currentRank / maxRank

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.sim.addRaidBuff(ShadowWeavingBuff(sp))
            }
        }

        override fun procs(sp: SimParticipant): List<Proc>  = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
