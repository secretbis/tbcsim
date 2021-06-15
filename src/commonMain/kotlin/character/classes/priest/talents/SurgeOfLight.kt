package character.classes.priest.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class SurgeOfLight(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Surge of Light"
        const val buffName = "Surge of Lights"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun critChanceModifier(): Double = -100.0

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val solBuff = object : Buff() {
            override val name: String = buffName
            override val durationMs: Int = 10000
            override val hidden: Boolean = true

            val consumeProc = solConsumeProc(this)

            override fun procs(sp: SimParticipant): List<Proc> = listOf(consumeProc)
        }

        fun solConsumeProc(buff: Buff) = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CAST
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(ability?.resourceCost(sp)?.toInt() ?: 0, Resource.Type.MANA, buffName)
                sp.consumeBuff(buff)
            }
        }

        val solProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 25.0 * currentRank

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(solBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(solProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
