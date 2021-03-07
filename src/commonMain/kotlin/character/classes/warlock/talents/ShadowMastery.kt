package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimIteration

class ShadowMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Mastery"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sim: SimIteration): Stats {
            return Stats(shadowDamageMultiplier = 1.0 + (0.1 * currentRank))
        }
    }

    override fun buffs(sim: SimIteration): List<Buff> = listOf(buff)
}
