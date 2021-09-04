package character.classes.priest.buffs

import sim.SimParticipant
import character.Ability
import character.Buff
import character.Proc
import character.Stats
import data.model.Item
import mechanics.Rating
import sim.Event

class InnerFocus : Buff() {
    companion object {
        const val name = "Inner Focus"
    }

    override val id = 14751
    override val name: String = Companion.name
    override val durationMs: Int = -1

    fun genCastRemovalProc(ifBuff: InnerFocus) = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_CAST
        )
        override val type: Type = Type.STATIC

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            if (ability == null || ability.name == name) return

            sp.consumeBuff(ifBuff)
        }
    }
    
    override fun modifyStats(sp: SimParticipant): Stats? {
        return Stats(spellCritRating = 25 * Rating.critPerPct)
    }

    override fun procs(sp: SimParticipant): List<Proc> = listOf(genCastRemovalProc(this))
}
