package character.classes.shaman.talents

import character.*
import data.model.Item
import mechanics.Rating
import sim.SimIteration

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
            override val name: String = Companion.name
            override val durationMs: Int = 10000
            override val hidden: Boolean = true

            override fun modifyStats(sim: SimIteration): Stats {
                return Stats(physicalCritRating = 3 * Rating.critPerPct)
            }
        }

        override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
            sim.addBuff(buff)
        }
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun procs(sim: SimIteration): List<Proc> = listOf(proc)
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
