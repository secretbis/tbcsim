package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class Backlash(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Backlash"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val critBuff = object : Buff() {
        override val name: String = "Backlash"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats? {
            val rating = currentRank * Rating.critPerPct
            return Stats(spellCritRating = rating)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(critBuff)
}
