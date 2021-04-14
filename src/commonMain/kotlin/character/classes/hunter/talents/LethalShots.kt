package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class LethalShots(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Lethal Shots"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(rangedCritRating = Rating.critPerPct * currentRank)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
