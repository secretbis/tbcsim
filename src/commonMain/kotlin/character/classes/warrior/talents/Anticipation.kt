package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Anticipation(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Anticipation"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "spell_nature_mirrorimage.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                defenseRating = Rating.defensePerPoint * (4 * currentRank)
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
