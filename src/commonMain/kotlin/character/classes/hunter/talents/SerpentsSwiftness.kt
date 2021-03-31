package character.classes.hunter.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class SerpentsSwiftness(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Serpent's Swiftness"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val rangedHasteBuff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHasteRating = 4.0 * Rating.hastePerPct * currentRank)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(rangedHasteBuff)

    fun petHastePct(): Double = 4.0 * currentRank
}
