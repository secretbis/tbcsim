package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimParticipant

class DualWieldSpecialization(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Dual Wield Specialization"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 3

    val buff = object : Buff() {
        override val name: String = "Dual Wield Specialization"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats? {
            // 2% hit per rank
            val modifier = currentRank
            val physicalHitRating = modifier * 2 * Rating.physicalHitPerPct

            // Only when dual wielding
            return if(sp.isDualWielding()) {
                Stats(physicalHitRating = physicalHitRating)
            } else null
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
