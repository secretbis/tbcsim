package character.classes.shaman.talents

import character.Buff
import character.Stats
import character.Talent
import sim.SimParticipant

class UnrelentingStorm(currentRank: Int) : Talent(currentRank) {
    companion object {
        const val name = "Unrelenting Storm"
    }
    override val name: String = Companion.name
    override val maxRank: Int = 5

    val buff = object : Buff() {
        override val name: String = "Unrelenting Storm"
        override val durationMs: Int = -1

        override fun modifyStats(sp: SimParticipant): Stats {
            return Stats(manaPer5Seconds = (sp.intellect() * 0.02 * currentRank).toInt())
        }
    }

    override fun buffs(sp: SimParticipant): List<Buff> = listOf(buff)
}
