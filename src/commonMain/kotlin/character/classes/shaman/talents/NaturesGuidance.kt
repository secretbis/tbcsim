package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class NaturesGuidance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Nature's Guidance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "Nature's Guidance"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val physicalHitRating = currentRank * Rating.meleeHitPerPct
            val spellHitRating = currentRank * Rating.spellHitPerPct
            return Stats(
                physicalHitRating = physicalHitRating,
                spellHitRating = spellHitRating
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
