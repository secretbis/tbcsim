package character.classes.shaman.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.Event
import sim.SimParticipant

class ElementalDevastation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Elemental Devastation"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val proc = object : Proc() {
        override val triggers: List<Trigger> = listOf(
            Trigger.SPELL_CRIT
        )
        override val type: Type = Type.STATIC

        val buff = object : Buff() {
            override val name: String = "Elemental Devastation"
            override val durationMs: Int = 10000
            override val hidden: Boolean = true

            override fun modifyStats(sp: SimParticipant): Stats {
                return Stats(meleeCritRating = 3 * Rating.critPerPct)
            }
        }

        override fun proc(sp: SimParticipant, items: List<Item>?, ability: Ability?, event: Event?) {
            sp.addBuff(buff)
        }
    }

    val buff = object : Buff() {
        override val name: String = "Elemental Devastation (static)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun procs(sp: SimParticipant): List<Proc> = listOf(proc)
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
