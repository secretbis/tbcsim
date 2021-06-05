package character.classes.mage.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ArcaneConcentration(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Arcane Concentration"
        const val buffName = "Clearcasting"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val ccBuff = object : Buff() {
            override val name: String = buffName
            override val durationMs: Int = -1
            override val hidden: Boolean = true

            val consumeProc = ccConsumeProc(this)

            override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
        }

        fun ccConsumeProc(buff: Buff) = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(ability?.resourceCost(sp)?.toInt() ?: 0, Resource.Type.MANA, buffName)
                sp.consumeBuff(buff)
            }
        }

        val ccProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_HIT,
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 2.0 * currentRank

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(ccBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(ccProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
