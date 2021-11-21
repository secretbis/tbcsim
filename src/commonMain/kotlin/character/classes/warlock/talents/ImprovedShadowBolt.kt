package character.classes.warlock.talents

import character.*
import data.model.Item
import sim.Event
import sim.SimParticipant

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

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val procBuff = object : Buff() {
        override val name: String = "Improved Shadow Bolt (static)"
        override val icon: String = "spell_shadow_shadowbolt.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val isbBuff = object : Buff() {
            override val name: String = "Improved Shadow Bolt"
            override val icon: String = "spell_shadow_shadowbolt.jpg"
            override val durationMs: Int = 12000
            override val maxCharges: Int = 4

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(shadowDamageMultiplier = 1.2)
            }

            val consumeProc = isbConsumeProc(this)

            override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
        }

        val proc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.WARLOCK_CRIT_SHADOW_BOLT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(isbBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(procBuff)
}
