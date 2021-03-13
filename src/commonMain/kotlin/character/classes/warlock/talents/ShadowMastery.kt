package character.classes.warlock.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class ShadowMastery(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Shadow Mastery"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = Companion.name
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(shadowDamageMultiplier = 1.0 + (0.1 * currentRank))
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
