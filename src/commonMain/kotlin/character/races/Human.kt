package character.races

import character.Ability
import character.Buff
import character.Race
import character.Stats
import mechanics.Rating
import sim.SimParticipant

class Human : Race() {
    // Humans have no modifications to baseline
    override var baseStats: Stats = Stats()

    val swordMaceSpec = object : Buff() {
        override val name: String = "Sword/Mace Specialization"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            val expertiseRating = 5.0 * Rating.expertiseRatingPerPoint
            return Stats(
                swordExpertiseRating = expertiseRating,
                maceExpertiseRating = expertiseRating
            )
        }
    }

    val theHumanSpirit = object : Buff() {
        override val name: String = "The Human Spirit"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                spiritMultiplier = 1.1
            )
        }
    }

    override fun racialByName(name: String): Ability? = null
    override fun buffs(sp: SimParticipant): List<Buff> = listOf(swordMaceSpec, theHumanSpirit)
}
