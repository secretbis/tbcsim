package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import character.classes.warrior.abilities.DefensiveStance
import mechanics.Rating
import sim.SimParticipant

class Defiance(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Defiance"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "ability_warrior_innerrage.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val isDefensiveStance = sp.buffs[DefensiveStance.name] != null
            return Stats(
                expertiseRating = 2 * currentRank * Rating.expertiseRatingPerPoint,
                innateThreatMultiplier = if(isDefensiveStance) { 1.0 + (0.05 * currentRank) } else 1.0
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
