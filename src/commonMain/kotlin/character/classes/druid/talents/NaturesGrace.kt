package character.classes.druid.talents

import character.Ability
import character.Buff
import character.Proc
import character.Talent
import data.model.Item
import sim.Event
import sim.SimParticipant

/**
 *
 */
class NaturesGrace(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Nature's Grace"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    val consumeProc = fun(buff: Buff): Proc {
        return object: Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.DRUID_CAST_STARFIRE,
                Trigger.DRUID_CAST_WRATH
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val postCritBuff = object : Buff() {
        override val name: String = "Nature's Grace"
        override val durationMs: Int = 15000
        override val maxCharges: Int = 1

        val proc = consumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val staticBuff = object : Buff() {
        override val name: String = "Nature's Grace (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val onCritProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(Trigger.SPELL_CRIT)
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(postCritBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(onCritProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff>  = listOf(staticBuff)
}