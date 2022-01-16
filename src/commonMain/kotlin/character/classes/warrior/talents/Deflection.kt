package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class Deflection(ranks: Int) : Talent(ranks) {
    companion object {
        const val name = "Deflection"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val icon: String = "ability_parry.jpg"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val parryPct = 1.0 * currentRank
            return Stats(parryRating = parryPct * Rating.parryPerPct)
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
