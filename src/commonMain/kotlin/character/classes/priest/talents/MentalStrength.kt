package character.classes.priest.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class MentalStrength(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Mental Strength"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1
        override val hidden: Boolean = true

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaMultiplier = 1.0 + (currentRank * 0.02))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
