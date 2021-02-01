package character.classes.warrior.talents

import character.Buff
import character.Stats
import character.Talent
import mechanics.Rating
import sim.SimIteration

class WeaponMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Weapon Mastery"
    }

    override val name: String = Companion.name
    override val maxRank: Int = 2

    val buff = object : Buff() {
        override val name: String = "Weapon Mastery"
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(expertiseRating = currentRank * Rating.expertisePerPct)
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
