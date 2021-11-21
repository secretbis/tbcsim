package character.classes.hunter.talents

import character.Buff
import character.CharacterType
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Surefooted(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Surefooted"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true
        override val icon: String = "ability_kick.jpg"

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(physicalHitRating = Rating.physicalHitPerPct * currentRank)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
