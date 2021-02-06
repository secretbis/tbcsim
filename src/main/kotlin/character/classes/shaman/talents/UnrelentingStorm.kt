package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class UnrelentingStorm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Unrelenting Storm"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(manaPer5Seconds = (sim.intellect() * 0.02 * currentRank).toInt())
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
