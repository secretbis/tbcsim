package character.classes.priest.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class SurgeOfLight(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Surge of Light"
        const val buffName = "Surge of Light"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 2

    val consumeProc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SMITE_CAST,
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addResource(ability?.resourceCost(sp)?.toInt() ?: 0, Resource.Type.MANA, Companion.name)
                sp.consumeBuff(buff)
            }
        }
    }

    val postCritBuff = object : Buff() {
        override val name: String = "Surge of Light"
        override val durationMs: Int = 10000
        override val maxCharges: Int = 1

        val proc = consumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val staticBuff = object : Buff() {
        override val name: String = "Surge of Light (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val onCritProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.PERCENT
            override fun percentChance(sp: SimParticipant): Double = 25.0 * currentRank

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(postCritBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(onCritProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(staticBuff)
}
