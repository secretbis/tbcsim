package character.classes.shaman.talents

import character.Buff
import character.Proc
import character.Stats
import character.Talent
import sim.SimIteration

class MentalQuickness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Mental Quickness"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    override val buffs: List<Buff> = listOf(
        object : Buff() {
            override val durationMs: Int = -1
            override val hidden: Boolean = true

            override fun modifyStats(sim: SimIteration, stats: Stats): Stats {
                val modifier = currentRank * 0.1
                val spellDamage = modifier * sim.subject.attackPower()
                return stats.add(Stats(spellDamage = spellDamage.toInt()))
            }

            override val procs: List<Proc> = listOf()
        }
    )

    override val procs: List<Proc> = listOf()

    // TODO: Apply this discount to instant spells
    fun instantManaCostMultiplier(): Double {
        return when(currentRank) {
            0 -> 1.0
            1 -> 1.13
            2 -> 1.26
            else -> 1.4
        }
    }
}
