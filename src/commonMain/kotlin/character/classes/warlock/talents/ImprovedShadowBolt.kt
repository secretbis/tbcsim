package character.classes.warlock.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimIteration

class ImprovedShadowBolt(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Improved Shadow Bolt"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun isbConsumeProc(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHADOW_DAMAGE_NON_PERIODIC
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.consumeBuff(buff)
            }
        }
    }

    val procBuff = object : Buff() {
        override val name: String = "Improved Shadow Bolt (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true


        val isbBuff = object : Buff() {
            override val name: String = "Improved Shadow Bolt"
            override val durationMs: Int = 12000
            override val maxCharges: Int = 4

            override fun modifyStats(sim: SimIteration): Stats {
                return Stats(shadowDamageMultiplier = 1.2)
            }

            val consumeProc = isbConsumeProc(this)

            override fun procs(sim: SimIteration): List<Proc> = listOf(consumeProc)
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.WARLOCK_CRIT_SHADOW_BOLT
            )
            override val type: Type = Type.STATIC

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?, event: Event?) {
                sim.addBuff(isbBuff)
            }
        }

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(procBuff)
}
