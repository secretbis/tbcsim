package character.classes.rogue.talents

import character.*
import mechanics.Rating
import sim.SimParticipant

class Precision(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Precision"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    fun hitRatingIncrease(): Double {
        return currentRank * 1.0 * Rating.physicalHitPerPct
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val icon: String = "ability_marksmanship.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = hitRatingIncrease())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
