package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class Ruin(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Ruin"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 1

    val critBuff = object : Buff() {
        override val name: String = "Ruin"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(spellDamageAddlCritMultiplier = 1.5)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(critBuff)
}
