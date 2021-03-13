package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class ElementalPrecision(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name: String = "Elemental Precision"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "Elemental Precision"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            // This isn't technically exactly equal to spell hit rating (no arcane, shadow, etc)
            // But for shaman it's good enough
            val spellHitRating = 2 * currentRank * Rating.spellHitPerPct
            return Stats(
                spellHitRating = spellHitRating
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
