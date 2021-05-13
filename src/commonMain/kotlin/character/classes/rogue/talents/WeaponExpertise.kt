package character.classes.rogue.talents

import character.*
import mechanics.Rating
import sim.SimParticipant

class WeaponExpertise(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Weapon Expertise"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    fun expertiseRatingIncrease(): Double {
        return currentRank * 5.0 * Rating.expertiseRatingPerPoint
    }

    val buff = object : Buff() {
        override val name: String = "${Companion.name} (Talent)"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(
                expertiseRating = expertiseRatingIncrease()
            )
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}