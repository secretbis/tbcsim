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

    val buff = object : Buff() {
        override val durationMs: Int = 10000
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
            return stats.add(Stats(physicalCritRating = 3 * Rating.critPerPct))
        }

        override val procs: List<Proc> = listOf()
    }

    override val procs: List<Proc> = listOf(
        object : Proc() {
            override val triggers: List<Trigger> = listOf(
                Trigger.SPELL_CRIT
            )

            override fun proc(sim: SimIteration, items: List<Item>?, ability: Ability?) {
                sim.addBuff(buff)
            }
        }
    )
}
