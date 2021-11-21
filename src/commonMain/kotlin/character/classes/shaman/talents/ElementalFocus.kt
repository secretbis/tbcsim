package character.classes.shaman.talents

import character.Ability
import character.Buff
import character.Proc
import character.Talent
import data.model.Item
import sim.Event
import sim.SimParticipant

class ElementalFocus(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Elemental Focus"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 1

    val consumeProc = fun(buff: Buff): Proc {
        return object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SHAMAN_CAST_LIGHTNING_BOLT,
                Trigger.SHAMAN_CAST_CHAIN_LIGHTNING,
                Trigger.SHAMAN_CAST_SHOCK
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.consumeBuff(buff)
            }
        }
    }

    val postCritBuff = object : Buff() {
        override val name: String = "Elemental Focus"
        override val icon: String = "spell_shadow_manaburn.jpg"
        override val durationMs: Int = -1
        override val maxCharges: Int = 2

        val proc = consumeProc(this)

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    val staticBuff = object : Buff() {
        override val name: String = "Elemental Focus (static)"
        override val icon: String = "spell_shadow_manaburn.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        val onCritProc = object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )
            override val type: Type = Type.STATIC

            override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
                sp.addBuff(postCritBuff)
            }
        }

        override fun procs(sp: SimParticipant): List<Proc> = listOf(onCritProc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(staticBuff)
}
