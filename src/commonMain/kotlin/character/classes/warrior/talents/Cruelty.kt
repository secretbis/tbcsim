package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Cruelty(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Cruelty"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "Cruelty"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val critPct = 1.0 * currentRank
            return Stats(meleeCritRating = critPct * Rating.critPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
