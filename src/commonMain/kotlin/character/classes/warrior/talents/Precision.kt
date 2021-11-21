package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Precision(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Precision"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "Precision"
        override val icon: String = "ability_marksmanship.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = currentRank * Rating.physicalHitPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
